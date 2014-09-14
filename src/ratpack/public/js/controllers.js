angular.module( "controllers", [] )

.controller( "UploadCtrl", [ "$scope", "$timeout", "imageService", function( $scope, $timeout, imageService ) {

	$scope.images = [];
	$scope.error = "";
	$scope.flash = "";
	$scope.uploading = false;

	var setError = function( error ) {
		$scope.error = error || "";
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
		function( data ) {
			$scope.imagePath = data.imagePath;
			$scope.images = data.images;
		},
		setError
	);

	$scope.uploadFile = function() {

		clearError();
		$scope.uploading = true;

		imageService.uploadImage( $scope.fileToUpload ).then(
			function( data ) {
				$scope.images.push( data.fileName );
				$scope.uploading = false;
				flashMessage( "Success!" );
			},
			setError
		);

	};

}])