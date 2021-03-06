# Covid Update
A mobile application which uses Covid-19 free API to provides daily updates on covid-19.
It is based on clean architecture & Android Architecture components (ViewModel, Livedata) and
coroutines.

It is also based on latest Material Design guidelines (BottomAppBar, AnimatedVecrtorDrawable,
font style, dark mode, etc.)

<img src="art/home.jpg" width="25%"></img>
<img src="art/error_place_holder.gif" width="25%"></img>

## Technologies & pattern
* Kotlin
* AndroidX
* Clean Architecture
* Android Architecture Components
* Retrofit/Okhttp - Type-safe HTTP client
* Gson - Serialization & deserialization
* Coroutine - Asynchronous and non-blocking programming
* Coil - Image loading
* Mockito - Mock dependencies
* Koin - Dependency injection
* MPAndroidChart - Chart views

## How to use it ?
Add a Koin.properties file inside assets folder and add the two following lines:
```
server_url=https://covid-193.p.rapidapi.com
api_key=YOUR_KEY
```

## Architecture

I tried to apply some of Clean Architecture pattern on this project. The app is divided into four
modules

### DataAdapter

This module includes providers which will call function of remote data service or locale data 
service. It also includes Adapters(which are implementation of providers). Adapters have the responsability to fetch data from remote or locale source. They handle exceptions(e.g IOException) and transform response into a business model or throw a dedicated exception.

### Domain

In this module the useCases are in charge to call the provider and transform the result (a business model or a dedicated exception) into an outcome<Model, Error> to be handled by presentation layer.

### Presentatiobn

Presentation is in charge to provide dedicated LiveDate or Event according to result received 
by the domain layer. It tranform business model into Data to be displayed by View layer.

### View

This module includes Activity, fragment or UI related widgets. It observes data from presentation layer and can call function of ViewModel.

## Licence
```
Copyright 2020 Gauvain Seigneur

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```