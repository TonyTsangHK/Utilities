package utils.stream;

import utils.string.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-11-11
 * Time: 15:35
 */
public class WindowCommandRunner {
    private String workingDirectory, output, error;
    private int exitValue;

    public WindowCommandRunner() {
        this(null);
    }

    public WindowCommandRunner(String workingDirectory) {
        this.workingDirectory = workingDirectory;

        this.output = null;
        this.error = null;
        this.exitValue = -1;
    }

    private ProcessBuilder prepareProcessBuilder(String command) {
        String osName = System.getProperty("os.name" );
        String[] cmd = new String[3];
        switch (osName) {
            case "Windows NT":
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = command;
                break;
            case "Windows 95":
                cmd[0] = "command.com";
                cmd[1] = "/C";
                cmd[2] = command;
                break;
            default:
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = command;
                break;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        if (!StringUtil.isEmptyString(this.workingDirectory)) {
            processBuilder.directory(new File(this.workingDirectory));
        }

        return processBuilder;
    }

    public void runCommand(String command) {
        try {
            ProcessBuilder processBuilder = prepareProcessBuilder(command);

            Process process = processBuilder.start();

            StreamGobbler errorGobbler = new
                StreamGobbler(process.getErrorStream(), StreamGobbler.Type.ERR, "Big5");

            StreamGobbler outputGobbler = new
                StreamGobbler(process.getInputStream(), StreamGobbler.Type.OUT, "Big5");

            outputGobbler.start();
            errorGobbler.start();

            // join them to current thread
            outputGobbler.join();
            errorGobbler.join();

            this.exitValue = process.waitFor();

            this.error = errorGobbler.getOutput();
            this.output = outputGobbler.getOutput();
        } catch (IOException | InterruptedException iox) {
            this.output = null;
            this.error = null;
            this.exitValue = -1;
        }
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public int getExitValue() {
        return exitValue;
    }
}
