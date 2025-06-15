import java.io.*;

public class Converter {
    public static void convert() {
        try {
            String currentDir = System.getProperty("user.dir");
            String classDir = currentDir + File.separator + "\\bin";
            String jarFile = currentDir + File.separator + "\\bin\\main.jar";
            String configFile = currentDir + File.separator + "\\bin\\config.xml";
            String exeOutput = currentDir + File.separator + "Ransom.exe";
            String mainClass = "Main";
            String launch4jExe = "C:\\Program Files (x86)\\Launch4j\\launch4jc.exe";

            new File(classDir).mkdirs();

            // ✅ Step 1: Compile Java files with --release 17 for compatibility
            ProcessBuilder compileBuilder = new ProcessBuilder(
                "javac", "--release", "17", "-d", classDir, "*.java"
            );
            compileBuilder.directory(new File(currentDir));
            compileBuilder.inheritIO();
            Process compile = compileBuilder.start();
            compile.waitFor();
            System.out.println("[✓] Compilation complete.");

            // Step 2: Create JAR
            ProcessBuilder jarBuilder = new ProcessBuilder(
                "jar", "cfe", jarFile, mainClass, "-C", classDir, "."
            );
            jarBuilder.inheritIO();
            Process jar = jarBuilder.start();
            jar.waitFor();
            System.out.println("[✓] JAR file created: main.jar");

            // Step 3: Write Launch4j config.xml
            PrintWriter writer = new PrintWriter(configFile);
            writer.println("<launch4jConfig>");
            writer.println("  <outfile>" + exeOutput + "</outfile>");
            writer.println("  <jar>" + jarFile + "</jar>");
            writer.println("  <dontWrapJar>false</dontWrapJar>");
            writer.println("  <headerType>console</headerType>");
            writer.println("  <jarArgs></jarArgs>");
            writer.println("  <chdir>.</chdir>");
            writer.println("  <priority>normal</priority>");
            writer.println("  <downloadUrl>https://java.com/download</downloadUrl>");
            writer.println("  <supportUrl></supportUrl>");
            writer.println("  <stayAlive>false</stayAlive>");
            writer.println("  <manifest></manifest>");
            writer.println("  <icon></icon>");

            // ✅ Require Java 17 or higher to run the EXE
            writer.println("  <jre>");
            writer.println("    <path></path>");
            writer.println("    <minVersion>17</minVersion>");
            writer.println("    <maxVersion></maxVersion>");
            writer.println("    <jdkPreference>preferJre</jdkPreference>");
            writer.println("    <runtimeBits>64/32</runtimeBits>");
            writer.println("  </jre>");

            writer.println("  <versionInfo>");
            writer.println("    <fileVersion>1.0.0.0</fileVersion>");
            writer.println("    <txtFileVersion>1.0</txtFileVersion>");
            writer.println("    <productVersion>1.0.0.0</productVersion>");
            writer.println("    <txtProductVersion>1.0</txtProductVersion>");
            writer.println("    <fileDescription>Java App</fileDescription>");
            writer.println("    <productName>JavaApp</productName>");
            writer.println("    <internalName>main</internalName>");
            writer.println("    <originalFilename>main.exe</originalFilename>");
            writer.println("    <companyName>YourCompany</companyName>");
            writer.println("    <copyright>YourCopyright</copyright>");
            writer.println("  </versionInfo>");

            writer.println("</launch4jConfig>");
            writer.close();
            System.out.println("[✓] config.xml created.");

            // Step 4: Run Launch4j
            ProcessBuilder launch4jBuilder = new ProcessBuilder(launch4jExe, configFile);
            launch4jBuilder.inheritIO();
            Process launch4j = launch4jBuilder.start();
            launch4j.waitFor();
            System.out.println("[✓] Executable created: main.exe");

        } catch (Exception e) {
            System.err.println("[✗] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
