package AoC.day17;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    @Data
    public static class CPU{
        long reg_A;
        long reg_B;
        long reg_C;
        List<Long> instructions;
        long program_counter = 0;
        List<Long> outStream = new ArrayList<>();

        public CPU(long reg_A, long reg_B, long reg_C, List<Long> instructions) {
            this.reg_A = reg_A;
            this.reg_B = reg_B;
            this.reg_C = reg_C;
            this.instructions = instructions;
        }

        public void reset(long initial_A) {
            this.reg_A = initial_A;
            this.reg_B = 0;
            this.reg_C = 0;
            this.program_counter = 0;
            outStream.clear();
        }

        public String run() {
            while(performOpcode()) {
                //you know perform it
            }
            return outStream.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        public boolean run_star2() {
            while (performOpcode()) {
                if(!new HashSet<>(instructions).containsAll(outStream)) {
                    return false;
                }
            }
            return instructions.equals(outStream);
        }

        private boolean performOpcode() {
            final long instr = readPC();
            final long opr = readPC();

            if(instr == -1) {
                return false;
            }
            if(instr != 4 && opr == -1) {
                return false;
            }

            switch ((int)instr) {
                case 0:
                    adv(opr);
                    break;
                case 1:
                    bxl(opr);
                    break;
                case 2:
                    bst(opr);
                    break;
                case 3:
                    jnz(opr);
                    break;
                case 4:
                    bxc();
                    break;
                case 5:
                    out(opr);
                    break;
                case 6:
                    bdv(opr);
                    break;
                case 7:
                    cdv(opr);
                    break;
            }

            return true;
        }

        private long readPC() {
            if(program_counter >= instructions.size()) {
                return -1;
            }
            long val = instructions.get((int)program_counter);
            program_counter++;
            return val;
        }

        private long getComboVal(long val) {
            switch ((int) val) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return val;
                case 4:
                    return reg_A;
                case 5:
                    return reg_B;
                case 6:
                    return reg_C;
                default:
                    assert false;
                    return -1;
            }
        }

        private void adv(long combo) {
            long val = getComboVal(combo);
            reg_A = div(val);
        }

        private void bxl(long literal) {
            reg_B ^= literal;
        }

        private void bst(long combo) {
            long val = getComboVal(combo);
            reg_B = val % 8;
        }

        private void jnz(long jmp) {
            if(reg_A == 0) {
                return;
            }
            program_counter = jmp;
        }

        private void bxc() {
            reg_B ^= reg_C;
        }


        private void out(long combo) {
            long val = getComboVal(combo) % 8;
            outStream.add(val);
        }

        private void bdv(long combo) {
            long val = getComboVal(combo);
            reg_B = div(val);
        }

        private void cdv(long combo) {
            long val = getComboVal(combo);
            reg_C = div(val);
        }

        private long div(long Value) {
            return (long)(reg_A / Math.pow(2, Value));
        }
    }

    public static CPU readInput(String filePath) {
        long a = 0;
        long b = 0;
        long c = 0;
        List<Long> program = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                a = Long.parseLong(line.replaceAll("[^0-9]", ""));
                line = reader.nextLine();
                b = Long.parseLong(line.replaceAll("[^0-9]", ""));
                line = reader.nextLine();
                c = Long.parseLong(line.replaceAll("[^0-9]", ""));
                line = reader.nextLine();
                line = reader.nextLine();
                String[] splitNums = line.split(" ")[1].split(",");
                program = Arrays.stream(splitNums).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return new CPU(a, b, c, program);
    }

    public static long walkBack(List<Long> program, List<Long> possible_As, int index) {
        if(index == program.size()) {
            return possible_As.get(0);
        }

        for(long A : possible_As) {
            List<Long> new_possibilities = findAforLastNums(program, A, index);
            if(!new_possibilities.isEmpty()) {
                long res = walkBack(program, new_possibilities, index + 1);
                if(res != -1) {
                    return res;
                }
            }
        }
        return -1;
    }

    public static List<Long> findAforLastNums(List<Long> program, long prevA, int index) {
        long newA = 0;
        List<Long> possible_new_A = new ArrayList<>();
        List<Long> checkList = program.subList(program.size() - index - 1, program.size());
        for(int i = 0; i < 8; i++) {
            newA = (prevA << 3) | i;
            CPU cpu = new CPU(newA, 0, 0, program);
            cpu.run();
            if(checkList.equals(cpu.outStream)) {
                System.out.println("New A found: " + newA + ", I = " + i);
                System.out.println(cpu.outStream);
                possible_new_A.add(newA);
            }
        }
        return possible_new_A;
    }

    // second a = 58
    public static long star2(CPU cpu) {
        List<Long> instructions = cpu.getInstructions();

        long val = walkBack(instructions, List.of(0L), 0);

        return val;
    }

    public static void main(String[] args) {
        CPU cpu = readInput("./src/main/java/AoC/day17/input.txt");
        String star1 = cpu.run();
        System.out.println("Star1: " + star1);

        long star2 = star2(cpu);
        System.out.println("Star2: " + star2);
    }

}
