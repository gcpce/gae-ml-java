# gae-ml-java
spring boot apps for App Engine to test out various Google Cloud ML APIs (e.g. Vision)

### Prep
Enable the storage, app engine, and vision APIs from the API dashboard

Download the `json` priviate key file for the service account that will be used for running this app on App Engine

to run locally, add the following environment variables in your execution shell or IDE:

* `PROJECT_ID` should be set to the GCP project ID where you have created your service account
* `GOOGLE_CREDENTIALS` should be set to the json keys downloaded
 for the service account that will be used by the app engine application
 
Create a bucket in your storage account and add a few images to it

### Usage
To run the vision test, if running locally or on app engine, put in the following query:

```http://localhost:8080/vision?bucket=<bucket_name>&file=<file_name>```

else if running on App Engine, enter:
```https://<app-sub-domain>.appspot.com/vision?bucket=<bucket_name>&file=<file_name>```

where `<bucket_name>` is the name of your bucket you just created and `<file_name>` is the name
an image file you've uploaded in that bucket

NOTE: you don't need to make the objects in the storage bucket public, but your service account should 
have access to them (which it normally does)

### Compiling & Deploying
to compile, type:

```mvn clean package```

if you get errors (most likely due to environment variables not being set), use the following:

```mvn clean package -DskipTests```

to deploy to app engine:

```mvn appengine:deploy -DskipTests```

if you don't want to skip tests, then add the required environment variables to the mvn command:

```mvn clean package -DPROJECT_ID=<projet_id> -DGOOGLE_CREDENTIALS=<credential json file>```

in the above line, the value for the credentials should be the actual contents of the json file, and not the filename of the file
