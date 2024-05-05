package com.cavemann.picturemanager.reader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cavemann.picturemanager.picture.Picture;

public class PictureReader {
	
	private static Logger log = LogManager.getLogger(PictureReader.class);
	
	private final List<String> pictureExtensions = Arrays.asList("jpg", "jpeg", "png");
	private final List<Picture> pictures;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

	public PictureReader(String rootFolderName) {
		pictures = readPictures(rootFolderName);
	}
	
	private List<Picture> readPictures(String rootFolderName) {
		
		File rootFolder = new File(rootFolderName);
		
		if (!rootFolder.exists()) {
			log.error("The path " + rootFolderName + " is invalid."); 
		}
		 
		return Arrays.asList(rootFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				final String[] tokens = name.split("\\.");
				final String extension = tokens[tokens.length - 1];
				if (pictureExtensions.contains(extension.toLowerCase())) {
					return true;
				}
				return false;
			}
		})).stream()
				.map(file -> new Picture(file, Integer.MIN_VALUE, null))
				.collect(Collectors.toList());	
	}
	
	public void distributePictures(boolean simulate) {
		log.info("Distribute pictures. Simulate = " + simulate);
		for (Picture picture : pictures) {
			BasicFileAttributes attr;
			File pictureFile = picture.getFile();
			try {
				attr = Files.readAttributes(pictureFile.toPath(), BasicFileAttributes.class);
				
				//remember size
				picture.setSize(attr.size());
				
				FileTime fileTime = attr.lastModifiedTime();
				Date creationDate = Date.from(fileTime.toInstant());
				String formattedCreationDate = dateFormat.format(creationDate);
				File dateFolder = new File(pictureFile.getParent() + "/" + formattedCreationDate);
				if (!simulate) {
					boolean created = dateFolder.mkdir();
					if (created) {
						log.info("Create folder '" + dateFolder.getName() + "'");
					}
				}
				
				// move the file
				log.info("Move file '" + pictureFile.getName() + "' to folder '" + dateFolder.getName() + "'");
				if (!simulate) {
					pictureFile.renameTo(new File(dateFolder.getPath() + "/" + pictureFile.getName()));
				}
			} catch (IOException e) {
				log.error("Cannot read attributes for the file path " + pictureFile.toPath());
			}
		}
	}
	
}
