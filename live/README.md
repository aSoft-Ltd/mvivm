# Live

## Introduction

Heavily inspired by LiveData, We need a kotlin multiplatform way to set data in the common main ViewModels and observe it in android, web, desktop, ios and any place where we can implement declarative
UI

## Problem Statement

While it is true that kotlin can has a SateFlow in the coroutines library, using that from non kotlin code (i.e. Swift and JavaScript) needs a lot of wrappers to work

## Setup : Gradle

```kotlin
dependencies {
    implementation("tz.co.asoft:live-core:<version>")
}
```

## Samples

```kotlin
val live = Live<Int>(1)
val watcher = liveInt.watch {
    println("Watcher 1: $it")
}   // console: "Watcher 1: 1"
live.value = 2 // console: "Watcher 1: 2"
watcher.stop() // or live.stop(watcher)
live.value = 3 // console: 
```

### Note:

this `Live<S>` should only be used in presentational layers. Its main purpose is to just change state for ui