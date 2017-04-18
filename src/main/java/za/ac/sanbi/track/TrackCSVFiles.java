package za.ac.sanbi.track;

/**
 * Created by hocine on 2017/04/18.
 */
public class TrackCSVFiles {

    private String filePath;
    private String fileName;
    private String fileOwner;
    private String fileType;

    public TrackCSVFiles(String filePath, String fileName, String fileOwner, String fileType) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileOwner = fileOwner;
        this.fileType = fileType;
    }

    public String getFilePath() { return filePath; }

    public String getFileName() { return fileName; }

    public String getFileOwner() {
        return fileOwner;
    }

    public String getFileType() {
        return fileType;
    }

}
