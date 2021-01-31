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
+ The start server button in the GUI app won't work if launched from shortcut created by default. (GUI must run from the app directory (barcode-reader) but the created shortcut starts it from the home directory(the icon is also the default java app icon, probably because the packager I've used: jpackage is an inclubator module and bugs haven't been worked out)).
+ If the start server button does not work, just run install-dir/bin/server directly. (default install directory is /opt/barcode-reader)
+ To start the server at boot, search Startup Applications, add bin/server to the list.
