
## Example implementation

http://litewave.electronics.fail

This is the Web App for the LiteWave Jugend forscht project.
The Code is based on Janis Streibs db-aufzug project (https://github.com/janisstreib/db-aufzug).
Especially the servlet framework is used by me. 

During development I will add my own code and try to reduce imported code as good as possible.

## OceanOptics

This project uses a OceanOptics STS-UV spectrometer. There is a java API available (OmniDriver),
unfortunately it's only compiled for x86 and x64 architectures. For ARM architectures like the Raspberry Pi 
there is a API called SeaBreeze available. I modified one of the cpp example codes to generated a spectrum.csv file in $PWD/data.
The code is executed by the java application which also parses the csv file after measurement.
This might be a bit bodgy but that's the only way to control the spectrometer using java on a ARM system.


## Development
The project is written in eclipse, so this is a eclipse project, that can be direcctly imported into eclipse.
### Dependencies
see lib/whatToPlaceHere.txt and static/js/whattoplacehere.txt
