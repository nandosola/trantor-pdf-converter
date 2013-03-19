libdir = File.dirname(File.expand_path(__FILE__))
extdir = File.join(libdir, 'ext')
$CLASSPATH << libdir

Dir["#{libdir}/*.jar"].each do |jar|
  require jar unless /[mc]libwrapper_j(ai|iio)\.jar/ =~ jar
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
  # (and frozen in ruby)
  ENV['LD_RUN_PATH'] << if ENV['LD_RUN_PATH'].nil?
                         ENV['JAIHOME']
                       else
                         ':'+ENV['JAIHOME']
                       end
  $CLASSPATH << ENV['JAIHOME']
  require "ext#{platform}/mlibwrapper_jai.jar"
  require "ext#{platform}/clibwrapper_jiio.jar"
end
