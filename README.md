# PixelImageDisplay_JavaFx

The objective of the code is to display the given pixel value files to the raw image.
For the uncompressed file, the pixel values are taken from the file given and directly converted into a 2-D image array. This array is then used to convert each value
into a Color object which is eventually displayed on an output Canvas.
For the compressed file, the values are read from the file and converted into a QuadTree. Once this is done, the QuadTree is converted to a 2D image array. The same process 
for the uncompressed file is followed after obtaining the 2D image array and the raw image gets displayed.
