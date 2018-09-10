git checkout gh-pages
git merge master

rm -rf resources/public
lein cljsbuild once min
make build-sass
cp -r resources/public/* .
git add -A

git commit -m "New deploy"
git push origin gh-pages
git checkout master
