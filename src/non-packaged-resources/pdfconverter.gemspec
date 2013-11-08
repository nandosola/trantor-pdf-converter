# parsed by Maven - leave it as is
Gem::Specification.new do |s|
  s.name = '${project.artifactId}'
  s.version = '${project.version}'
  s.platform = 'java'

  s.summary = 'Transform TIFFs and docs to PDF'
  s.description = 'This gem is a JRuby wrapper to transform TIFFs and LibreOffice-suported doc formats to PDF'
  s.homepage = '${project.url}'

  s.authors = ['Nando Sola']
  s.email = ['nando@abstra.cc']

  s.files = Dir['copyleft/lesser.html']
  s.licenses << 'GNU Lesser General Public License, Version 3.0'
  s.files += Dir['./LICENSE']
  s.files += Dir['3rd-party-licenses/**/*']
  s.files += Dir['lib/*.jar']
  s.files += Dir['lib/**/*']
end
