"use strict";

angular.module( "uploader.controllers" )

.controller( "UploadCtrl", [ "$scope", "$timeout", "imageService", function( $scope, $timeout, imageService ) {

	$scope.images = [];
	$scope.error = "";
	$scope.flash = "";
	$scope.uploading = false;

	var setError = function( error ) {

		// check error.data in case we're looking at an http response, then check for an error string, and
		// then default it to a blank string.
		$scope.error = error.data || error || "";
		$scope.uploading = false;
		
	};

	var clearError = function() {
		setError("");
	};

	var flashMessage = function( message, duration ) {

		$scope.flash = message;

		$timeout(function() {
			$scope.flash = "";
		}, duration || 3000);

	};

	imageService.getImages().then(
		function( response ) {
			$scope.imagePath = response.data.imagePath;
			$scope.images = response.data.images;
		},
		setError
	);

	$scope.uploadFile = function() {

		clearError();
		$scope.uploading = true;

		imageService.uploadImage( $scope.fileToUpload ).then(
			function( response ) {
				$scope.images.push( response.data.fileName );
				$scope.uploading = false;
				flashMessage( "Success!" );
			},
			setError
		);

	};

}])