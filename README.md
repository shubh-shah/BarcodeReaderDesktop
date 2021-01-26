# BarcodeReaderDesktop
Read barcodes and feed them to your desktop with your mobile.   
Transform your phone into a barcode reader.   
No need to buy a scanner.   
Get the mobile app [here](https://github.com/shubh-shah/BarcodeReaderMobile).
Get a release from github or you can build it yourself (Requires JDK15).

### Windows
+ Might need [Visual C++ Resistributable 2015](https://www.microsoft.com/en-in/download/details.aspx?id=48145 "Link to Microsoft download page") on windows to run because of how jpackage makes native launchers.
+ Needs network access(Obviously).
+ To start the server at boot, add a shortcut to server.exe in the startup directory (%USER_DIR%\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup).

### Linux
+ The app must run in the directory above bin(Barcode Reader) in linux, GUI shortcut created by default does not have a working start server button (Because it doesn't start the app in the directory I expected)
+ If the start app button does not work, just run server from the bin directory.
+ To start the server at boot, go to settings and search Startup Applications, add server to the list.
+ Haven't really used on linux but it should theoretically work.
