package es.lanyu.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class Util {

	/**Vuelca una cadena en un fichero con la codificacion marcada
	 * @param informe Cadena a escribir
	 * @param fichero Ruta del fichero
	 * @param charSet Codificacion a usar
	 * @throws IOException
	 */
	public static void guardarInformeEnFichero(String informe, String fichero, String charSet) throws IOException{
		guardarInformeEnFichero(informe, new OutputStreamWriter(new FileOutputStream(fichero), charSet));
	}
	/**Vuelca una cadena en un fichero con la codificacion UTF-8
	 * @param informe Cadena a escribir
	 * @param fichero Ruta del fichero
	 * @throws IOException
	 */
	public static void guardarInformeEnFichero(String informe, String fichero) throws IOException{
		guardarInformeEnFichero(informe, fichero, "UTF-8");
	}
	/**Vuelca una cadena en un fichero con la codificacion UTF-8
	 * @param informe Cadena a escribir
	 * @param fichero File donde escribir la cadena
	 * @throws IOException
	 */
	public static void guardarInformeEnFichero(String informe, File fichero) throws IOException{
		guardarInformeEnFichero(informe, new FileWriter(fichero));
	}
	//Es mas eficiente escribir rodeando el Writer con un BufferedWriter
	//VER: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedWriter.html
	private static void guardarInformeEnFichero(String texto, Writer escritor) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(escritor);
		bWriter.write(texto);
//		bWriter.flush();
		bWriter.close();
	}
	
	//TODO A�adir cadena a archivo
	public static void agregarTextoEnFinalArchivo (String texto, Writer escritor){
		System.out.println("Hay que implementarlo");
	}
	
	public static void copiarArchivo(String origen, String destino) throws IOException{
		Files.copy(FileSystems.getDefault().getPath(origen),
	    		FileSystems.getDefault().getPath(destino),
	    		StandardCopyOption.REPLACE_EXISTING);
	}
	
	//Codigo conseguido de http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	public static long contarLineas(String fichero) throws IOException {
		long lineas = 0;
		
		//Respuesta de @Ernestas Gruodis NO REQUIERE JAVA 8 y es muy rapido 
//	    try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(fichero), 1024)) {
//
//	        byte[] c = new byte[1024];
//	        boolean empty = true,
//	                lastEmpty = false;
//	        int read;
//	        while ((read = is.read(c)) != -1) {
//	            for (int i = 0; i < read; i++) {
//	                if (c[i] == '\n') {
//	                    lineas++;
//	                    lastEmpty = true;
//	                } else if (lastEmpty) {
//	                    lastEmpty = false;
//	                }
//	            }
//	            empty = false;
//	        }
//
//	        if (!empty) {
//	            if (lineas == 0) {
//	                lineas = 1;
//	            } else if (!lastEmpty) {
//	                lineas++;
//	            }
//	        }
//	    }
		
		//Respuesta de @msayag REQUIERE JAVA 8 y suele ser aun mas rapido
		try (Stream<String> lines = Files.lines(new File(fichero).toPath(), Charset.defaultCharset())) {
			lineas = lines.count();
		}
	    
		return lineas;
	}
	
	public static File[] archivosConExtension (String rutaDirectorio, String extension) throws Exception {
		if (rutaDirectorio != null){
			File[] archivos = null;
			File dir = new File(rutaDirectorio);
			if(dir.isDirectory()){
				archivos = dir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(extension);
					}
				});
			}
			else {
				throw new Exception("La ruta no pertenece a un directorio " + rutaDirectorio);
			}
				
			return archivos;
		}
		else {
			throw new Exception("La ruta no puede ser nula");
		}
	}
	
	public static File getDirectorioDeClase (Class<?> clase) {
		File directorio = null;
		String Recurso = clase.getSimpleName() + ".class";
         try {
             URL url = clase.getResource(Recurso);
             if (url.getProtocol().equals("file")) {
                 File f = new File(url.toURI());
                 do {
                     f = f.getParentFile();
                 } while (!f.isDirectory());
                 directorio = f;
             } else if (url.getProtocol().equals("jar")) {
                 String expected = "!/" + Recurso;
                 String s = url.toString();
                 s = s.substring(4);
                 s = s.substring(0, s.length() - expected.length());
                 File f = new File(new URL(s).toURI());
                 do {
                     f = f.getParentFile();
                 } while (!f.isDirectory());
                 directorio = f;
             }
         } catch (Exception e) {
             directorio = new File("");//".");//El punto puede perjudicar
         }
         
	     return directorio;
	}
}
