//digunakan untuk menutup secara otomatis jdialog
private Timer createCloseTimer(int seconds) {
    ActionListener close = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (dialog.getContentPane().getComponentCount() == 1
                        && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                        dialog.dispose();
                    }
                }
            }
        }
    };
    Timer t = new Timer(seconds * 1000, close);
    t.setRepeats(false);
    return t;
}
//mendapatkan ekstensi file
private static String getFileExtension(File file) {
    String name = file.getName();
    try {
        return name.substring(name.lastIndexOf(".") + 1);
    } catch (Exception e) {
        return "";
    }
}
//fungsi untuk copy file menggunakan metode stream
private static void copyFileUsingStream(File source, File dest) throws IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } finally {
        is.close();
        os.close();
    }
}
//mendapatkan nama file
private static String getFileName(String name){
    try {
        String a= name.substring(name.lastIndexOf(".") + 1);
        return name.replace("."+a, "");
    } catch (Exception e) {
        return "";
    }
}
//tulis file
public void writeFile(String in, File fout){
    FileOutputStream fos = null;
    try {
        fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        
        String[] tmpStr=in.split("\n");
        
        bw.write(tmpStr[0]);
        bw.newLine();
        bw.write(tmpStr[1]);
        bw.newLine();
        bw.write(tmpStr[2]);
        bw.close();
        
    } catch (Exception e) {
        Logger.getLogger(ScannerPublikasi.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(ScannerPublikasi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
//cara baca folder
public void readFolder(){
    File f =new File(pathSubFolder);
    if(f.exists() && f.isDirectory()){
        File[] listOfFiles=f.listFiles();
        for (File listOfFile : listOfFiles) {

        }
    }
}
//fungsi please wait
public void pleaseWait(){
    Thread t = new Thread(new Runnable(){
        @Override
        public void run(){
            JOptionPane.showMessageDialog(null, "Finish","Please wait...",1);
        }
    });
    t.start();
}
//Send file + text ke server (http) | Dependensi class : files/MultipartUtility.java
public List<String> send(String user, String nama_file, String id_file, String no_publikasi, File file_upload, String path_pdf){
    try {
        MultipartUtility multipart = new MultipartUtility(this.requestURL, this.charset);
        multipart.addFormField("nama_file", nama_file);
        multipart.addFormField("user", user);
        multipart.addFormField("id_file", id_file);
        multipart.addFormField("no_publikasi", no_publikasi);
        multipart.addFormField("path_pdf", path_pdf);
        multipart.addFilePart("xml_file", file_upload);

        List<String> response = multipart.finish();
        return response;
    } catch (IOException ex) {
        Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
//Nonton direktori | Dependensi class : files/WatchDir.java
public static void main(String[] argt) throws IOException {
   String[] args={"-r","C:\\Users\\rahmat\\Documents\\NetBeansProjects\\XMLBuilder\\data\\0000"};
   if (args.length == 0 || args.length > 2)
       usage();
   boolean recursive = false;
   int dirArg = 0;
   if (args[0].equals("-r")) {
       if (args.length < 2)
           usage();
       recursive = true;
       dirArg++;
   }

   Path dir = Paths.get(args[dirArg]);
   new WatchDir(dir, recursive).processEvents();
}
// JFileChooser
public void jFileChooser(){
    JFileChooser fc=new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int i=fc.showOpenDialog(null); 
    if(i==JFileChooser.APPROVE_OPTION){  
        File rootFolder = fc.getSelectedFile();
        folderOutput.setText(rootFolder.getPath());
    }
}