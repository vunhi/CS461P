# PSU Locator 
Copyright © 2017 Nhi Vu

# Description

The PSU locator is an android application that helps users navigate their way through the large Portland State campus, specifically by helping the user to locate a specific room in the campus. There are 3 key features in this mobile app: 1) an interactive map, 2) a search function, and 3) a favorites page. The user can navigate through the campus and find information for a building through the interactive campus map, they can search for a room through the search function, and they can save locations to a Favorites page for future use. 

As of this release the PSU Locator has been successfully implemented 2 buildings on the campus: Cramer Hall and Neuberger Hall. Each building has 2 floors each with 2 rooms to demonstrate the functionality. 

# Build
The Minimum API Level for the PSU Locator applicataion is 22. There are two options to run this application on your android device: 

1. Download the apk for the application through this link: https://drive.google.com/open?id=0BxqL-8RsXVhpeHNtOHVsN09KakE and install on your phone. 

2. Using an Android development environment, Clone or Download the source code from the github repository (https://github.com/vunhi/CS461P)and build the application and run it on your device or emulator. 

# Copyright

Copyright © 2017 Nhi Vu, Victor Diego

This project was adapted from another Office Locator application that was specifically designed for the Nike campus for a Capstone class for which I was a part of. This was also an open source project and the source code for that version can be found here: https://github.com/cnorton18/capstone2017/tree/Markers. Although this PSU Locator was based off of this group capstone project, I have taken only source code that I and my partner Victor Diego worked on and have added my own additional code to it. He has granted me permission to use his work in this application. 

Copyright © 2017 Portland State University 

The Portland State logo and campus maps and floor plans were taken from the official Portland State University website pdx.edu through these specific pages: https://www.pdx.edu/university-communications/download-psu-logo and https://www.pdx.edu/floorplans/campus-map respectively. 

Copyright © 2017 Portland State University 

Copyright © 2014 anorth 

A portion of the source code used to provide pinch-zoom capabilities in the app was taken from the open source project https://gist.github.com/anorth/9845602. 

# License 

This project is licensed under the MIT License - see the LICENSE.md file for details.

# Contact
Nhi Vu - vunhi@pdx.edu

# Week 3 Project Update

Right now I have successfully implemented the interactive map aspect of the the PSU locator for one floor for one building on the campus. This is for the Cramer building. So when the user clicks on this building in the map they are taken to the basement floorplan of Cramer.

Now I must incorporate the option of choosing different floors and different rooms for each building. 

For the remainder of the term that is what I will be working on as well as adding the search functionality and improving the ease of use of the app as well as the general app theme. If I have time I hope to accomplish this for every floor and room number for each building (though that is quite a lot of buildings!). I also need to finish filling out the project ReadMe; as you can see, it's pretty bare right now. 

Some difficulties I'm facing: figuring out how to incorporate the interactive navigation bar for each page. Also, I find that working with the android xml files is very difficult. Using the design function is not as intuitive as it seems: dragging widgets across the screen is not entirely smooth and it can throw off the layout of all other widgets on the screen. And even though the layout appears one way on the projected screen in AS it is soemtimes not the same as what appears on the actual phone screen. This is a very stressful obstacle to overcome. 


