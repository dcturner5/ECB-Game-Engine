package com.gammarush.engine;

import java.io.File;
import java.text.NumberFormat;

public class SystemManager {
	private Runtime runtime = Runtime.getRuntime();

    public String Info() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.OsInfo());
        sb.append(this.MemInfo());
        sb.append(this.DiskInfo());
        return sb.toString();
    }

    public String OSname() {
        return System.getProperty("os.name");
    }

    public String OSversion() {
        return System.getProperty("os.version");
    }

    public String OsArch() {
        return System.getProperty("os.arch");
    }

    public long totalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public String MemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("FREE: ");
        sb.append(format.format(freeMemory / 1024) + ", ");
        sb.append("ALLOCATED: ");
        sb.append(format.format(allocatedMemory / 1024) + ", ");
        sb.append("MAX: ");
        sb.append(format.format(maxMemory / 1024) + "");
        return sb.toString();

    }

    public String OsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(this.OSname());
        sb.append("<br/>");
        sb.append("Version: ");
        sb.append(this.OSversion());
        sb.append("<br/>");
        sb.append(": ");
        sb.append(this.OsArch());
        sb.append("<br/>");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append("<br/>");
        return sb.toString();
    }

    public String DiskInfo() {
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        StringBuilder sb = new StringBuilder();

        /* For each filesystem root, print some info */
        for (File root : roots) {
            sb.append("File system root: ");
            sb.append(root.getAbsolutePath());
            sb.append("<br/>");
            sb.append("Total space (bytes): ");
            sb.append(root.getTotalSpace());
            sb.append("<br/>");
            sb.append("Free space (bytes): ");
            sb.append(root.getFreeSpace());
            sb.append("<br/>");
            sb.append("Usable space (bytes): ");
            sb.append(root.getUsableSpace());
            sb.append("<br/>");
        }
        return sb.toString();
    }
}
