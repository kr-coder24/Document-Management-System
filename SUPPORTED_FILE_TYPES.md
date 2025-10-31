# Supported File Types

## Text-Based Files (Full Support)

These file types are **fully supported** and will display correctly in the view and edit dialogs:

### Plain Text Files
- `.txt` - Plain text files (Best support)
- `.log` - Log files
- `.csv` - Comma-separated values
- `.md` - Markdown files
- `.xml` - XML files
- `.json` - JSON files
- `.properties` - Java properties files
- `.ini` - Configuration files
- `.conf` - Configuration files
- `.yaml` / `.yml` - YAML configuration files
- `.html` - HTML files (displays as text)
- `.css` - CSS stylesheet files
- `.js` - JavaScript files
- `.java` - Java source files
- `.py` - Python source files
- `.cpp` / `.c` / `.h` - C/C++ files
- `.sql` - SQL query files

## Binary Files (Not Supported)

These file types will **NOT work properly** and will display as "rubbish" or corrupted data:

### Image Files
- `.jpg` / `.jpeg` - JPEG images
- `.png` - PNG images
- `.gif` - GIF images
- `.bmp` - Bitmap images
- `.svg` - Scalable Vector Graphics

### Document Files
- `.pdf` - PDF documents
- `.doc` / `.docx` - Microsoft Word documents
- `.xls` / `.xlsx` - Microsoft Excel spreadsheets
- `.ppt` / `.pptx` - Microsoft PowerPoint presentations
- `.odt` - OpenDocument text files
- `.ods` - OpenDocument spreadsheets

### Archive Files
- `.zip` - ZIP archives
- `.rar` - RAR archives
- `.7z` - 7-Zip archives
- `.tar` / `.tar.gz` - Tar archives

### Multimedia Files
- `.mp3` - Audio files
- `.wav` - Wave audio files
- `.mp4` - Video files
- `.avi` - Video files
- `.flv` - Flash video

### Other Binary Files
- `.exe` - Executable programs
- `.dll` - Dynamic link libraries
- `.so` - Shared object files
- `.bin` - Binary files
- `.dat` - Data files

## Why Binary Files Don't Work

The application reads file content as **plain text** using `BufferedReader` and `FileInputStream`. When you upload binary files:

1. The bytes are interpreted as text characters
2. Binary data gets corrupted during text conversion
3. Non-UTF-8 byte sequences cause encoding errors
4. The result is unreadable/garbled content

## Recommendation

**Upload only text-based files** to this document management system. The system is designed for:
- Document text editing and viewing
- Source code storage
- Configuration file management
- Log file viewing
- Data file storage (CSV, JSON, XML)

If you need to store binary files (images, PDFs, documents), consider:
1. Converting them to PDF/images separately and managing them outside this system
2. Using a file storage service (cloud storage)
3. Storing metadata about the files instead of the files themselves
