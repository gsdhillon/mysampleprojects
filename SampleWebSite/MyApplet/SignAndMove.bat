cd "D:\Gurmeet\SampleWebProject\MyApplet"
jarsigner -storepass password .\dist\MyApplet.jar SampleKey
copy .\dist\MyApplet.jar ..\SampleWeb\web\
copy .\dist\lib\*.jar ..\SampleWeb\web\lib\
pause