import groovy.io.FileType
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import ratpack.form.Form
import ratpack.jackson.JacksonModule

import static java.util.UUID.randomUUID
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

	bindings {
		add new JacksonModule()
	}

	handlers {

		get {
			render file( "public/index.html" )
		}

		prefix("image") {

			get {

				File imageDirectory = new File( launchConfig.getOther( "uploadDirectory", null ) )
				List images = []

				imageDirectory.eachFile( FileType.FILES ) { images << it.name }

				render json( images )

			}

			post("upload") {

				Form form = parse Form
				def uploaded = form.file("fileUpload")

				if( uploaded.contentType.type.contains("image") ) {

					String fileName = "${randomUUID()}.png"
					String uploadDirectory = launchConfig.getOther( "uploadDirectory", null )
					String imagePath = "$uploadDirectory/$fileName"
					String thumbPath = "$uploadDirectory/thumb/$fileName"
					BufferedImage image = ImageIO.read( uploaded.inputStream )
					BufferedImage thumb = Scalr.resize( image, Mode.FIT_TO_HEIGHT, 100 )

					ImageIO.write( image, "png", new File( imagePath ) )
					ImageIO.write( thumb, "png", new File( thumbPath ) )

					render json( fileName: fileName )

				} else {

					response.status 400
					response.send "Invalid file type. Images only!"

				}

			}

		}

		assets "public"

	}

}