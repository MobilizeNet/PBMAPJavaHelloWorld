#./bin/bash
cd WebApp/sampleSite/sampleSite-angular
echo "Installing Dependencies"
yarn
echo "Building Angular code"
ng build
cp -r ..\wwwroot ..\..\..\ReferenceApplication\src\main\webapp
echo "Done!"