cmake_minimum_required(VERSION 3.10.2)

project("sneakershop")

add_library(
        native-lib
        SHARED
        sneakershop.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        ${log-lib})
