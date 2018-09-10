watch-sass:
	sass --scss --watch resources/public/css/sass/main.scss:resources/public/css/main.css

build-sass:
	sass --scss resources/public/css/sass/main.scss:resources/public/css/main.css

deploy-gh:
	./deploy.sh
