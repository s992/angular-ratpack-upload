package org.swalsh.image

import com.google.inject.Inject
import com.google.inject.Singleton
import groovy.io.FileType
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import ratpack.exec.ExecControl
import ratpack.exec.Promise
import ratpack.form.UploadedFile

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import static java.util.UUID.randomUUID

@Singleton
class ImageService {

	private final ExecControl execControl

	@Inject
	ImageService( ExecControl execControl ) {
		this.execControl = execControl
	}

	Promise<List<String>> getUploadedImages( File imageDirectory ) {

		execControl.blocking {
			imageDirectory.listFiles( { it.isFile() } as FileFilter ).sort { it.lastModified() }*.name
		}

	}

	Boolean isImageFile( UploadedFile file ) {
		file.contentType.type.contains( "image" )
	}

	Promise<File> process( UploadedFile file, File imageDirectory, File thumbDirectory ) {

		String fileName = getUniqueFileName( "png" )
		BufferedImage image = readImage( file )

		execControl.blocking {
			saveThumb( image, fileName, thumbDirectory )
			saveImage( image, fileName, imageDirectory )
		}

	}

	String getUniqueFileName( String extension ) {
		"${randomUUID()}.$extension"
	}

	BufferedImage readImage( UploadedFile file ) {
		ImageIO.read( file.inputStream )
	}

	File saveImage( BufferedImage image, String fileName, File directory ) {

		File file = new File( directory, fileName )
		ImageIO.write( image, "png", file )

		file

	}

	File saveThumb( BufferedImage image, String fileName, File directory ) {

		BufferedImage thumb = Scalr.resize( image, Mode.FIT_TO_HEIGHT, 100 )
		saveImage( thumb, fileName, directory )

	}

}