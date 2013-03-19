# create by maven - leave it as is
Gem::Specification.new do |s|
  s.name = 'imageutils'
  s.version = '0.0.1'
  s.platform = 'java'

  s.summary = 'PDF image utils (experimental)'
  s.description = 'This gem wraps a Java Library to transform scanned image docs (PDF, TIF, GIF, JPG, PNG) into PDF/A-1b. It has some extra features too'
  s.homepage = 'http://github.com/nandosola/imageutils'

  s.authors = ['Nando Sola']
  s.email = ['nando@abstra.cc']

  s.files = Dir['copyleft/lesser.html']
  s.licenses << 'GNU Lesser General Public License, Version 3.0'
  s.files += Dir['./LICENSE']
  s.files += Dir['3rd-party-licenses/**/*']
  s.files += Dir['lib/*.jar']
  s.files += Dir['lib/**/*']
end
