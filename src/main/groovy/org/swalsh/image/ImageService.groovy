package org.swalsh.image

import groovy.io.FileType
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import ratpack.form.UploadedFile
import static java.util.UUID.randomUUID

class ImageService {

	List getUploadedImages( String imagePath ) {

		File imageDirectory = new File( imagePath )
		List images = []

		imageDirectory.eachFile( FileType.FILES ) { images << it }
		images = images.sort { it.lastModified() }.collect { it.name }

		images

	}

	Boolean isImageFile( UploadedFile file ) {
		file.contentType.type.contains( "image" )
	}

	File process( UploadedFile file, String imageDirectory ) {

		String fileName = getUniqueFilename( "png" )
		BufferedImage image = readImage( file )
		File fullSize = saveImage( image, fileName, imageDirectory )
		File thumb = saveThumb( image, fileName, imageDirectory )

		fullSize

	}

	String getUniqueFilename( String extension ) {
		"${randomUUID()}.$extension"
	}

	BufferedImage readImage( UploadedFile file ) {
		ImageIO.read( file.inputStream )
	}

	File saveImage( BufferedImage image, String fileName, String imageDirectory ) {

		File file = new File( "$imageDirectory/$fileName" )
		ImageIO.write( image, "png", file )

		file

	}

	File saveThumb( BufferedImage image, String fileName, String imageDirectory ) {

		BufferedImage thumb = Scalr.resize( image, Mode.FIT_TO_HEIGHT, 100 )
		saveImage( thumb, fileName, "$imageDirectory/thumb" )

	}

}