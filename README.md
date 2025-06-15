# Ransim
A java based Ransomware Simulation for Educational Purposes.

# Security Simulation Setup

This project demonstrates a basic simulation involving Java-based `.exe` file generation, remote upload, and dynamic creation of a macro-enabled Word document with embedded VBA pointing to the uploaded file.

---

## Step-by-step Instructions

# Pre-Requisits

### 1. Create a Folder (Currently set to Encrypt a folder in C Drive, C:\\SecuritySimulation) Modifiable

Create a directory to store the simulation files:

### 2. Add Test Files

Place a few test files inside this folder for encryption or processing. (Example.txt)

### 3. Download Launch4j and Create a Dropbox Account

1. Download Lauch4j and make sure that it is in this location `C:\Program Files (x86)\Launch4j`
2. Make account on DropBox and get Access token from there

---

### 4. Set CMD as the Default Shell 

In VsCode
1. Press `Ctrl + Shift + P` (or F1)
2. then type: `Terminal: Select Default Profile`
3. From the list, choose: `Command Prompt`

### 5. Open Word and Enable Macro

1. Go to:
`File > Options > Trust Center > Trust Center Settings > Macro Settings`
2. Then:
 Check `Trust access to the VBA project object model`
3. Click OK, then restart Word.

### 6. Open the (Created folder where these Files are placed) and compile the program

1. Open folder (With the whole program) in vscode
2. Open vscode's terminal and run following commands
    ```
    javac -d bin -cp .;Application/lib/jacob.jar Application/*.java Ransom/*.java
    java -cp bin;Application/lib/jacob.jar Application

    ```
3. If everything went well, application's GUI will appear in from of you
4. You can get word file in `output` folder 

---

## How to Run

1. Compile the program
2. It should open the Attackers GUI 
3. Generate an Api Key from Dropbox and paste it. (For uploading Ransom to Cloud) (Fetched Later on)
4. Click ```Generate Word File``` to create a word file with required text (Modiafiable)
5. Word File should show up in an ```Output``` Folder in the same directory as this program.
6. Send the ```.docm``` file to Virtual Machine and Click ```Enable Content``` to run the script. 

---

## Notes

- Ensure Microsoft Word macros are enabled to test `.docm` functionality.
- This project is for educational and simulation purposes only. Do **not** use it for any unauthorized or malicious activity.

---

## Resources

- [Launch4j](http://launch4j.sourceforge.net/)
- [JACOB (Java COM Bridge)](https://sourceforge.net/projects/jacob-project/)
- [Dropbox (Cloud Service)](https://www.dropbox.com/)


