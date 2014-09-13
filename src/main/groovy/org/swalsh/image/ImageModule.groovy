package org.swalsh.image

import com.google.inject.AbstractModule
import com.google.inject.Scopes

class ImageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind( ImageService.class ).in( Scopes.SINGLETON )
	}

}