package com.easyclaim.EasyClaimBackend.DataAccess;

import com.easyclaim.EasyClaimBackend.Entity.LifeClaim;
import com.easyclaim.EasyClaimBackend.UseCase.DeleteLifeClaimDataAccessInterface;
import com.easyclaim.EasyClaimBackend.UseCase.GetFilteredLifeClaimsDataAccessInterface;
import com.easyclaim.EasyClaimBackend.UseCase.GetLifeClaimsDataAccessInterface;
import com.easyclaim.EasyClaimBackend.UseCase.UploadLifeClaimDataAccessInterface;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AllClaimsDataAccessObject implements GetLifeClaimsDataAccessInterface, DeleteLifeClaimDataAccessInterface,
         UploadLifeClaimDataAccessInterface, GetFilteredLifeClaimsDataAccessInterface {
    @Override
    public List<LifeClaim> getLifeClaims(String type) throws ExecutionException, InterruptedException {
        ArrayList<LifeClaim> claims = new ArrayList<LifeClaim>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String collectionName = type + " Claims";
        Iterable<DocumentReference> refs = dbFirestore.collection(collectionName).document("Life")
                .collection("Claims").listDocuments();
        for (DocumentReference ref: refs) {
            ApiFuture<DocumentSnapshot> futureSnapshot = ref.get();
            DocumentSnapshot doc = futureSnapshot.get();
            if (doc.exists()) {
                claims.add(doc.toObject(LifeClaim.class));
            }
        }
        if (!claims.isEmpty()) {
            return claims;
        } else {
            return null;
        }
    }

    @Override
    public String deleteLifeClaim(String type, String claimNumber) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(type + " Claims").document("Life").collection("Claims")
                .document(claimNumber).delete();

        return claimNumber;

    }

    @Override
    public LifeClaim findLifeClaimOfType(String type, String claimNumber) throws ExecutionException, InterruptedException {
        // Creating iterable object for current life claims
        LifeClaim currentClaim = null;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> refs = dbFirestore.collection(type + " Claims").document("Life")
                .collection("Claims").listDocuments();

        // Iterating through all current life claims
        for (DocumentReference ref: refs) {
            ApiFuture<DocumentSnapshot> futureSnapshot = ref.get();
            DocumentSnapshot doc = futureSnapshot.get();

            // Parsing document into LifeClaim object and checking claimNumber attribute
            if (doc.exists()) {
                currentClaim = doc.toObject(LifeClaim.class);
                assert currentClaim != null;
                if (currentClaim.getClaimNumber().equals(claimNumber)) {
                    break;
                }
            }
        }
        return currentClaim;
    }

    @Override
    public LifeClaim findLifeClaim(String claimNumber) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef_current = dbFirestore.collection("Current Claims").document("Life").collection("Claims").document(claimNumber);
        ApiFuture<DocumentSnapshot> future_current = docRef_current.get();
        DocumentSnapshot document_current = future_current.get();

        DocumentReference docRef_historical = dbFirestore.collection("Historical Claims").document("Life").collection("Claims").document(claimNumber);
        ApiFuture<DocumentSnapshot> future_historical = docRef_historical.get();
        DocumentSnapshot document_historical = future_historical.get();
        if (document_historical.exists()) {
            return document_historical.toObject(LifeClaim.class);
        } else if (document_current.exists()){
            return document_current.toObject(LifeClaim.class);
        } else{
            return null;
        }
    }

    @Override
    public String uploadLife(String type, LifeClaim claim) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(type + " Claims").document("Life").collection("Claims")
                .document(claim.getClaimNumber()).set(claim);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public List<LifeClaim> getFilteredLifeClaims(String type) throws InterruptedException, ExecutionException {
        ArrayList<LifeClaim> filteredClaims = new ArrayList<>();
        Map<String, Boolean> filters = stringToDictionary(type);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String collectionName = "Historical Claims";
        CollectionReference refs = dbFirestore.collection(collectionName).document("Life")
                .collection("Claims");

        applyAmountFilter(filters, refs, filteredClaims);
        applyDateFilter(filters, refs, filteredClaims);

        if (!filteredClaims.isEmpty()) {
            return filteredClaims;
        } else {
            return getLifeClaims("Historical");
        }
    }

    private void applyAmountFilter(Map<String, Boolean> filters, CollectionReference refs, ArrayList<LifeClaim> filteredClaims)
            throws InterruptedException, ExecutionException {
        if (filters.get("1")) {
            addClaimsFromQuery(refs.whereGreaterThan("generalLoanInformation.loanA.amountOfInsuranceAppliedFor", 0)
                    .whereLessThan("generalLoanInformation.loanA.amountOfInsuranceAppliedFor", 25000).get(), filteredClaims);
        }

        if (filters.get("2")) {
            addClaimsFromQuery(refs.whereGreaterThan("generalLoanInformation.loanA.amountOfInsuranceAppliedFor", 25000)
                    .whereLessThan("generalLoanInformation.loanA.amountOfInsuranceAppliedFor", 50000).get(), filteredClaims);
        }

        if (filters.get("3")) {
            addClaimsFromQuery(refs.whereGreaterThan("generalLoanInformation.loanA.amountOfInsuranceAppliedFor", 50000).get(), filteredClaims);
        }

        if (filters.get("4") && !filters.get("1") && !filters.get("2") && !filters.get("3")) {
            addClaimsFromQuery(refs.whereGreaterThan("dateOccured", subtractOneMonth()).get(), filteredClaims);
        }
    }

    private void applyDateFilter(Map<String, Boolean> filters, CollectionReference refs, ArrayList<LifeClaim> filteredClaims)
            throws InterruptedException, ExecutionException {
        if (filters.get("5") && !filters.get("1") && !filters.get("2") && !filters.get("3")) {
            addClaimsFromQuery(refs.whereGreaterThan("dateOccured", subtractLastSixMonths()).get(), filteredClaims);
        }

        if (filters.get("6") && !filters.get("1") && !filters.get("2") && !filters.get("3")) {
            addClaimsFromQuery(refs.whereGreaterThan("dateOccured", subtractLastYear()).get(), filteredClaims);
        }

        if (filters.get("7") && !filters.get("1") && !filters.get("2") && !filters.get("3")) {
            addClaimsFromQuery(refs.whereLessThan("dateOccured", subtractLastYear()).get(), filteredClaims);
        }
    }

    private void addClaimsFromQuery(ApiFuture<QuerySnapshot> future, ArrayList<LifeClaim> filteredClaims)
            throws InterruptedException, ExecutionException {
        QuerySnapshot querySnapshot = future.get();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            if (document.exists() && !filteredClaims.contains(document.toObject(LifeClaim.class))) {
                filteredClaims.add(document.toObject(LifeClaim.class));
            }
        }
    }

    private static Map<String, Boolean> stringToDictionary(String inputString) {
        Map<String, Boolean> result = new HashMap<>();

        String[] keyValuePairs = inputString.split("&");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=");
            String key = keyValue[0];
            String value = keyValue[1];

            // Convert 'true' or 'false' strings to boolean
            boolean booleanValue = value.equals("true");

            result.put(key, booleanValue);
        }

        return result;
    }

    private static String subtractOneMonth() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Subtract one month
        LocalDate oneMonthAgo = today.minusMonths(1);

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the resulting date
        String formattedDate = oneMonthAgo.format(formatter);

        return formattedDate;
    }

    private static String subtractLastSixMonths() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Subtract six months
        LocalDate lastSixMonths = today.minusMonths(6);

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the resulting date
        String formattedDate = lastSixMonths.format(formatter);

        return formattedDate;
    }

    private static String subtractLastYear() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Subtract one year
        LocalDate lastYear = today.minusYears(1);

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the resulting date
        String formattedDate = lastYear.format(formatter);

        return formattedDate;
    }
}
