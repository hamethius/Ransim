# Pre Requisits
## Downloads
1. Download Lauch4j and make sure that it is in this location `C:\Program Files (x86)\Launch4j`
2. Make account on DropBox and get Acess token from there


## Set CMD as the Default Shell 
In VsCode
1. Press `Ctrl + Shift + P` (or F1)
2. then type: `Terminal: Select Default Profile`
3. From the list, choose: `Command Prompt`


## Open Word 
1. Go to:
`File > Options > Trust Center > Trust Center Settings > Macro Settings`
2. Then:
 Check `Trust access to the VBA project object model`
3. Click OK, then restart Word.

# Instructions 
1. Open this folder in vscode
2. Open vscode's terminal and run following commands
    ```
    javac -d bin -cp .;Application/lib/jacob.jar Application/*.java Ransom/*.java
    java -cp bin;Application/lib/jacob.jar Application

    ```
3. If everything went well, application's GUI will appear in from of you
4. You can get word file in `output` folder 