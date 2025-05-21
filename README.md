# Ransim
A java based Ransomware Simulation for Educational Purposes.

# Security Simulation Setup

This project demonstrates a basic simulation involving Java-based `.exe` file generation, remote upload, and dynamic creation of a macro-enabled Word document with embedded VBA pointing to the uploaded file.

---

## Step-by-step Instructions

### 1. Create a Folder

Create a directory to store the simulation files:
### 2. Add Test Files

Place a few test files inside this folder for encryption or processing.

### 3. Download and Install Launch4j

Download and install [Launch4j](http://launch4j.sourceforge.net/) to:
### 4. Compile and Run `Converter.java`

Open the folder containing `Converter.java` and run the following commands:
This will generate an `.exe` file in the current directory.

### 5. Test the Generated `.exe`

Run the generated `.exe` file to test the simulation.

### 6. Use `WordVbaAutomation.java`

This Java program creates a macro-enabled Word document (`.docm`) with embedded VBA code.

To compile and run it:
Make sure `jacob.jar` is present in the `lib` directory.

---

## Objective

1. Generate the `.exe` file using `Converter.java`.
2. Upload this `.exe` file to a remote server.
3. Edit the VBA macro URL inside `WordVbaAutomation.java` to point to the uploaded `.exe` file.
4. Generate the `.docm` file using `WordVbaAutomation.java`.

---

## Notes

- Ensure Microsoft Word macros are enabled to test `.docm` functionality.
- This project is for educational and simulation purposes only. Do **not** use it for any unauthorized or malicious activity.

---

## Resources

- [Launch4j](http://launch4j.sourceforge.net/)
- [JACOB (Java COM Bridge)](https://sourceforge.net/projects/jacob-project/)
- [Jumpshare Trimming Help](https://support.jumpshare.com/article/155-how-to-trim-your-video)


