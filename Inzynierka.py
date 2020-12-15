import keyboard as keyboard
import serial

"""Opening of the serial port"""

arduino = serial.Serial("COM3")

"""Initialising variables"""
rawdata = []
while True:  # making a loop

    rawdata.append(str(arduino.readline()))
    print(rawdata)
    if keyboard.is_pressed('q'):
        break
"""Receiving data and storing it in a list"""



def clean(L):  # L is a list
    newl = []  # initialising the new list
    for i in range(len(L)):
        temp = L[i][2:]
        newl.append(temp[:-3])
    return newl


cleandata = clean(rawdata)


def write(L):
    file = open("data.txt", "w")
    for i in range(1, len(L)):
        file.write(L[i] + '\n')
        print(L[i])
    file.close()


write(cleandata)
