cd "D:\Senergy\SmartTime\SmartTimeApplet"
jarsigner -storepass password .\dist\SmartTimeApplet.jar SampleKey
copy .\dist\SmartTimeApplet.jar ..\SmartTimeWeb\web\
copy .\dist\lib\*.jar ..\SmartTimeWeb\web\lib\
pause


cd "D:\Senergy\SmartTime 05012013\SmartTimeApplet"