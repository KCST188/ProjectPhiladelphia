import keyboard
import serial
import cv2
from os import startfile

"""Opening of the serial port"""

arduino = serial.Serial("COM3")

"""Initialising variables"""
rawdata = []
length = 0
clip = 'test.mov'
# getting list length as duration of the clip times 2
cap = cv2.VideoCapture(clip)
fps = cap.get(cv2.CAP_PROP_FPS)  # OpenCV2 version 2 used "CV_CAP_PROP_FPS"
frame_count = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
duration = frame_count / fps
length = duration * 2
length = round(length)
cap.release()
filmStart = True
while True:  # making a loop
    # receiving data from arduino
    rawdata.append(str(arduino.readline()))
    print(rawdata)
    # breaking loop after clicking 'q' or if there is enough data
    if len(rawdata) == length + 1 or keyboard.is_pressed('q'):
        break
    # starting clip
    if filmStart is True:
        startfile(clip)
        filmStart = False
"""Receiving data and storing it in a list"""


def clean(L):  # L is a list
    newl = []  # initialising the new list
    for i in range(len(L)):
        temp = L[i][2:]  # clear 2 first signs
        newl.append(temp[:-3])  # clear last 3 signs
    return newl


cleandata = clean(rawdata)


# write data to file
def write(L):
    file = open("../../OneDrive/Desktop/data.txt", "w")
    for i in range(1, len(L)):
        file.write(L[i] + '\n')
    print(L)
    file.close()


write(cleandata)
