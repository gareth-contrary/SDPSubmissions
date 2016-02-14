package sml;

/**
 * @author Gareth Moore
 */

public class BnzInstruction extends Instruction {
    private int register;
    private String label2;

    public BnzInstruction(String label1, String opcode) {
        super(label1, opcode);
    }

    public BnzInstruction(String label1, int register, String label2) {
        super(label1, "bnz");
        this.register = register;
        this.label2 = label2;
    }

    @Override
    public void execute(Machine m) {
        int value = m.getRegisters().getRegister(register);
        if (value != 0) {
        	Labels labels = m.getLabels();
        	int pc = labels.indexOf(label2);
        	m.setPc(pc);
        } 
    }

    @Override
    public String toString() {
        return super.toString() + " register " + register + " to " +label2;
    }
}
