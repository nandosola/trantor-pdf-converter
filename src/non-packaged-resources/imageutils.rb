libdir = File.dirname(File.expand_path(__FILE__))
extdir = File.join(libdir, 'ext')
$CLASSPATH << libdir

Dir["#{libdir}/*.jar"].each do |jar|
  # JRuby includes all the openssl stuff from 1.7.3 onwards
  unless JRUBY_VERSION >= '1.7.3' && /bcprov-jdk15\.jar/ =~ jar
    require jar
  end
end

platform = ''
if java.lang.System.get_property('os.name').downcase.include?('linux')
  p 'Using JAI & Java ImageIO native extensions'
  platform << '/linux'
  platform << if java.lang.System.get_property('os.arch').end_with?('64')
                '-amd64'
              else
                '-i586'
              end
  ENV['JAIHOME'] = extdir + platform

  # ld_library_path is bad: http://xahlee.info/UnixResource_dir/_/ldpath.html
  ENV['LD_RUN_PATH'] = ENV['JAIHOME']

  $CLASSPATH << ENV['JAIHOME']
  require "ext#{platform}/mlibwrapper_jai.jar"
  require "ext#{platform}/clibwrapper_jiio.jar"
end
