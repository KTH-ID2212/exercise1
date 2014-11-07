package se.kth.id2212.ex1.compute;

import java.io.File;

public class Spy implements Task
{
    private static final long serialVersionUID = -7386258182412348165L;
    private String[] fileNames = null;

    public String[] getFileNames()
    {
        return fileNames;
    }

    public void execute()
    {
        String rootDir = "/";

        if (File.pathSeparatorChar == '\\')
        {
            // DOS server
            rootDir = "C:\\";
        }

        File f = new File(rootDir);
        fileNames = f.list();
    }
}
