PATH=$JAVA_HOME/bin:$PATH

VM_ARGS="-Xmx32M"

java ${VM_ARGS} -cp "libs/*" org.fixt.fixcracker.gui.swing.FixCrackerSwingGUI