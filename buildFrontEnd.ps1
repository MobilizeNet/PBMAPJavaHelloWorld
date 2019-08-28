cd WebApp\sampleSite\sampleSite-angular
Write-Host "Installing Dependencies"
yarn
Write-Host "Building Angular code"
ng build
xcopy ..\wwwroot ..\..\..\ReferenceApplication\src\main\webapp /E /Y
Write-Host "Done!"