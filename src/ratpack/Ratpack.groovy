import java.awt.image.BufferedImage
import org.swalsh.image.*
import ratpack.form.Form
import ratpack.form.UploadedFile
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

	bindings {
		add new JacksonModule()
		add new ImageModule()
	}

	handlers {

		get {
			render file( "public/index.html" )
		}

		prefix("image") { ImageService imageService ->

			String imageDirectory = launchConfig.getOther( "uploadDirectory", null )

			get {
				render json( imageService.getUploadedImages( imageDirectory ) )
			}

			post("upload") {

				Form form = parse Form
				UploadedFile uploaded = form.file( "fileUpload" )

				if( imageService.isImageFile( uploaded ) ) {

					File imageFile = imageService.process( uploaded, imageDirectory )
					render json( fileName: imageFile.name )

				} else {

					response.status 400
					response.send "Invalid file type. Images only!"

				}

			}

		}

		assets "public"

	}

}