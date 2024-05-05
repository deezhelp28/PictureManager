package com.cavemann.picturemanager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.cavemann.picturemanager.reader.PictureReader;

public class Main {

	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("The program needs to get the absolute path of the folder with the pictures as the first argument.");
			return;
		}
		
		Configurator.setRootLevel(Level.ERROR);
		
		boolean simulate = false;
		for (String arg : args) {
			switch (arg) {
				case "-h" :
				case "-help" : {
					System.out.println("The first argument is the absolute path of the folder containing the pictures to be sorted.\n"
							+ "-simulate			Runs the program outputting the changes that it will do, without actually doing them.\n"
							+ "-verbose			Output more information when running the program.");
					return;
				}
				case "-verbose" : {
					Configurator.setRootLevel(Level.INFO);
					break;
				}
				case "-simulate" : {
					simulate = true;
					Configurator.setRootLevel(Level.INFO);
					break;
				}
				default : {
					
				}
			}
		}
		
		PictureReader reader = new PictureReader(args[0]);
		reader.distributePictures(simulate);
	}

}
