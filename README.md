# Telemetry Ticketing System Simulator

Electric Vehicle Telemetry: A system for tracking and analyzing electric vehicle driving with multiple servers on a network socket and RESTful web services.


![Firefly an electric car that drives fast and passes police camera radar at night and policeman 38885(1)](https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/1f920326-a10a-4348-80d6-87bbff41f8fe)


## Documentation - table of contents
<ul>
  <li><a href="#technologies">Technologies</a></li>
  <li><a href="#description">Project description</a></li>
  <li><a href="#architecture">Architecture</a></li>
  <li><a href="#more">More details</a></li>
</ul>

<div id="technologies"></div>

## Technologies

<div class="flex">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/3fb6d9b3-8fac-42b9-b283-5f54f3915151" height="50" style="margin-right: 20px;">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/f9f44f3c-3cea-4cef-87ef-30e7502518f5" height="50" style="margin-right: 20px;">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/f6a837a3-c4bd-478f-a5d5-8bd178351f49" height="50" style="margin-right: 20px;">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/d341a775-334b-498e-bbc2-806316ec8963" height="50" style="margin-right: 20px;">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/f9e3077e-c5ef-4cd8-9570-042eb820f5a3" height="50" style="margin-right: 20px;">
<img src="https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/6be1116d-5366-450d-bfe6-677dd2840d71" height="50">
</div>

<ul>
    <li><strong>Java:</strong> General-purpose programming language known for its portability and performance.</li>   
    <li><strong>Maven:</strong> Build automation that simplifies the build process and manages project dependencies.</li>
    <li><strong>Payara:</strong> Open-source Java application server platform based on GlassFish.</li>
    <li><strong>Docker:</strong> Platform for developing, shipping, and running applications in containers.</li>
    <li><strong>H2 database:</strong> Lightweight and fast relational database management system written in Java.</li>
    <li><strong>HSQL database:</strong> Feature-rich relational database management system suitable for embedded deployments.</li>
</ul>

<div id="description"></div>

## Project description
The system is designed to simulate driving electric vehicles in a city using real data stored in CSV files. It starts by specifying a file name and then reads and processes all the data. 
This information is sent to radar systems located throughout the city, which are positioned based on GPS coordinates. When a vehicle enters the range of an active radar, the system checks if it is speeding. 
If the vehicle exceeds the speed limit defined by the radar (e.g., driving faster than 40 km/h for more than 7 seconds without any abnormal behavior), the radar issues a speeding ticket and stores the details in a database.

The data file for electric vehicle driving contains rows of data, with the following column names in the first row: 
Milliseconds since Epoch, Speed, Watt, Ampere, Altitude, GPS Speed, Vehicle Body Temperature, Battery Percentage, Battery Voltage, Battery Capacity, Battery Temperature, Remaining Mileage, Total Mileage, Latitude, and Longitude.

![image](https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/2953557a-5820-426c-a6cc-c6e51f293ad6)



### Features:
<ul>
  <li>
    <strong>Reading driving data (multithreaded):</strong>
    <ul>
      <li>Data is recorded at very short intervals (less than 1 second).</li>
      <li>Each row contains GPS coordinates, speed, time, and other vehicle parameters.</li>
    </ul>
  </li>
  <li>
    <strong>Simulating data transmission:</strong>
    <ul>
      <li>Data from the CSV file is treated as "live" data.</li>
      <li>Each row is sent one by one to the telemetry and speed control system.</li>
      <li>The time difference between rows is multiplied by a time correction factor.</li>
    </ul>
  </li>
  <li>
    <strong>Speed measurement radars:</strong>
    <ul>
      <li>Placed at different locations with specified GPS coordinates.</li>
      <li>Radars process data if the vehicle is within their range.</li>
      <li>They check if the speed exceeds the allowed limit and for how long.</li>
    </ul>
  </li>
  <li>
    <strong>Recording violations:</strong>
    <ul>
      <li>If the vehicle exceeds the speed limit, the radar records the data.</li>
      <li>If the overspeeding lasts longer than the allowed time, new data is recorded with a "false" status.</li>
      <li>If the overspeeding persists, a speeding ticket is generated.</li>
    </ul>
  </li>
  <li>
    <strong>Communication between servers:</strong>
    <ul>
      <li><strong>RadarServer:</strong>
        <ul>
          <li>Registers with the RegistrationServer.</li>
          <li>Opens a network socket and operates in a multithreaded mode.</li>
          <li>Creates virtual threads for each client and processes commands.</li>
        </ul>
      </li>
      <li><strong>PenaltyServer:</strong>
        <ul>
          <li>Opens a network socket and operates in a single-threaded mode.</li>
          <li>Stores penalty data and retrieves penalties/statistics as needed.</li>
        </ul>
      </li>
      <li><strong>VehicleServer:</strong>
        <ul>
          <li>Opens a network socket and operates in a multithreaded mode.</li>
          <li>Creates virtual threads for each client and checks if the vehicle is within a radar's range.</li>
          <li>Sends commands to the RadarServer.</li>
        </ul>
      </li>
    </ul>
  </li>
  <li>
    <strong>VehicleSimulator:</strong>
    <ul>
      <li>Connects to the VehicleServer.</li>
      <li>Reads the data file row by row.</li>
      <li>Prepares data for commands and sends them asynchronously without waiting for a response.</li>
    </ul>
  </li>
  <li>
    <strong>Docker Containerization:</strong>
    <ul>
      <li>Everything is containerized using Docker.</li>
      <li>Deployment is streamlined with Docker Compose (`docker-compose up` command).</li>
    </ul>
  </li>
  <li>
    <strong>Database Usage:</strong>
    <ul>
      <li>Utilizes two databases for storing various data.</li>
    </ul>
  </li>
  <li>
    <strong>Multithreading:</strong>
    <ul>
      <li>Uses multithreading extensively for efficient data processing and communication.</li>
    </ul>
  </li>
  <li>
    <strong>User Interface:</strong>
    <ul>
      <li>Powered by Payara.</li>
      <li>Allows visualization of simulation data stored in database, active radars, vehicle statuses, data filtering and much more.</li>
    </ul>
  </li>
</ul>


<div id="architecture"></div>

## Architecture

System scheme:

![image](https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/75840191-7547-4873-90bc-a82a61490e60)
![image](https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/ce102bbe-ae19-4a1e-8617-4cc1952ab2e2)

System installation architecture and methods of RESTful web services:

![image](https://github.com/CroAnna/telemetry-tickets-radar/assets/90924342/45db8b75-7f15-45c7-9e20-e774580c2a1a)


<div id="more"></div>

## More details

Find out more about this project in <a href="https://github.com/CroAnna/telemetry-tickets-radar/blob/main/project-description.pdf">project requirements file</a>.



