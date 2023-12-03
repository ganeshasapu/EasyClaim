<div align='center'>
    <h1><b>EasyClaim</b></h1>
    <h2>by Ganesh Asapu, Nauhar Kapur, Raunak Madan, Vibhas Raizada, and Caesar Saleh</h2>
</div>

_Making the adjudication process a breeze_

_______

<div>
  Presenting EasyClaim, a platform to streamline and digitize the adjudication process for <em><b>Securian Canada</b></em>.
  <br><br>
  This project was made as part of the <em><b>Technology Leadership Initative's</b></em> integrated-industry project at the <em><b>University of Toronto</b></em>.
</div>


<div>
  <h3> Our expected features <em>(WIP)</em>: </h3>

  <ul>
    <li> An inbox page to view claims submitted by policyholders </li>
    <li> A 'historical claims' page to view previous </li>
    <li> Viewable claims that feature submitted pdf's and a side display listing the main </li>
    <ul>
      <li> An algorithm that displays claims similar to the claim being currently viewed, and the recommended next steps in the process </li>
      <li> Assisting in processing claims faster based on precedent </li>
    </ul>
  </ul>
</div>

<div>
  <h3> Tech Stack: </h3>
  <ul>
    <li> Frontend in <em>React</em> and <em>Typescript</em> </li>
    <li> Backend in <em>Spring Boot</em> and <em>Java</em></li>
    <li> Database running on <em>Firebase</em></li>
  </ul>
</div>

<div>
  <h3> Main folder components: </h3>
  <ul>
    <li> <code> backend </code> </li>
      <ul>
        <li> Contains Folders following th Clean Arhcitecure Engine:</li>
          <ul>
            <li>Controllers</li>
            <li>Entity</li>
            <li>Firebase Initializer</li>
            <li>Services</li>
          </ul>
        <li>Spring Boot connection test file</li>
      </ul>
    <li> <code> dummy_data </code> </li>
      <ul>
        <li> Completed JSON files for: </li>
          <ul>
            <li>Disability Claims</li>
            <li>Employment Claims</li>
            <li>Life Claims</li>
          </ul>
        <li> Each JSON file contains <em>count</em> number of mock models based on specific number in <code>json_generator.py</code> </li>
      </ul>
    <li> <code> frontend </code> </li>
      <ul>
        <li> An app folder containg: </li>
          <ul>
            <li>Additional Components frontend file</li>
            <li>Database 'Historical Claims' page frontend file</li>
            <li>Inbox page frontend file</li>
          </ul>
      </ul>
    <li> <code> JSON Models </code> </li>
      <ul>
        <li> Blank JSON files for: </li>
          <ul>
            <li>Disability Claims</li>
            <li>Employment Claims</li>
            <li>Life Claims</li>
          </ul>
      </ul>
    <li> <code> json_generator.py </code> </li>
      <ul>
        <li>Python file use to automatically generate filled out JSON claim mock files</li>
      </ul>
  </ul>
</div>

_______

<div>
  <h3> Using json_generator to create JSON mock files: </h3>
  <ol>
    <li> Open <code>json_generator.py</code></li>
    <li> In the main branch at the bottom of the file, specifiy the count for the number of dummy JSON models you would like </li>
    <li> Run the file in your favourite compiler!</a> </li>
    <li> Check the <code>dummy_data</code> folder; each file in the folder contains <em>count</em> number of mock models, all placed into one compliation file for each type of claim</li>
  </ol>
</div>

<div>
  <h3> <em>(Old)</em> Running the program locally: </h3>
  <ol>
    <li> Install <a href="https://nodejs.org/en/"> node.js </a> </li>
    <li> Run <code>npm run dev</code> in the Terminal</li>
    <li> Open <a href="http://localhost:3000"> http://localhost:3000 </a> </li>
  </ol>
  <h3> <em>(Update)</em> Hooray! The program is now viewable at <a href="https://easyclaim.vercel.app"> this link! </a> </h3>
</div>


