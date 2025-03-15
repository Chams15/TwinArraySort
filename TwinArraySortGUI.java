import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class TwinArraySortGUI extends JFrame {
    private JTextArea outputArea;
    private JButton sortButton;
    private JTextField uniqueValuesInput;
    private JTextField arraySizeInput;  // New field
    private static final int MAX_ARRAY_SIZE = 10000000;  // Changed from ARRAY_SIZE
    
    public TwinArraySortGUI() {
        setTitle("TwinArray Sort Algorithm - Optimized");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        sortButton = new JButton("Sort Array");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSorting();
            }
        });
        
        uniqueValuesInput = new JTextField(String.valueOf(MAX_ARRAY_SIZE / 2), 10);
        uniqueValuesInput.setToolTipText("Enter number of unique values (1-" + MAX_ARRAY_SIZE + ")");

        // Add input verification
        uniqueValuesInput.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    int value = Integer.parseInt(uniqueValuesInput.getText());
                    if (value < 1 || value > MAX_ARRAY_SIZE) {
                        JOptionPane.showMessageDialog(null, 
                            "Please enter a value between 1 and " + MAX_ARRAY_SIZE);
                        uniqueValuesInput.setText(String.valueOf(MAX_ARRAY_SIZE / 2));
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number");
                    uniqueValuesInput.setText(String.valueOf(MAX_ARRAY_SIZE / 2));
                }
            }
        });

        // Array Size Input
        arraySizeInput = new JTextField(String.valueOf(MAX_ARRAY_SIZE), 10);
        arraySizeInput.setToolTipText("Enter array size (1-" + MAX_ARRAY_SIZE + ")");
        
        // Add input verification for array size
        arraySizeInput.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    int value = Integer.parseInt(arraySizeInput.getText());
                    if (value < 1 || value > MAX_ARRAY_SIZE) {
                        JOptionPane.showMessageDialog(null, 
                            "Please enter a value between 1 and " + MAX_ARRAY_SIZE);
                        arraySizeInput.setText(String.valueOf(MAX_ARRAY_SIZE));
                    }
                    // Update unique values maximum
                    int uniqueMax = Math.min(Integer.parseInt(arraySizeInput.getText()), MAX_ARRAY_SIZE);
                    uniqueValuesInput.setToolTipText("Enter number of unique values (1-" + uniqueMax + ")");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number");
                    arraySizeInput.setText(String.valueOf(MAX_ARRAY_SIZE));
                }
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Array Size: "));
        inputPanel.add(arraySizeInput);
        inputPanel.add(Box.createHorizontalStrut(15));  // Add spacing
        inputPanel.add(new JLabel("Unique Values: "));
        inputPanel.add(uniqueValuesInput);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(sortButton, BorderLayout.NORTH);
        controlPanel.add(inputPanel, BorderLayout.SOUTH);
        
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void executeSorting() {
        // Add try-with-resources or finally block
        try {
            // Validate array size
            int arraySize;
            try {
                arraySize = Integer.parseInt(arraySizeInput.getText());
                if (arraySize < 1 || arraySize > MAX_ARRAY_SIZE) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter an array size between 1 and " + MAX_ARRAY_SIZE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid array size");
                return;
            }
            
            // Validate unique values
            int uniqueValues;
            try {
                uniqueValues = Integer.parseInt(uniqueValuesInput.getText());
                if (uniqueValues < 1 || uniqueValues > arraySize) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter unique values between 1 and " + arraySize);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for unique values");
                return;
            }
            
            int[] array = generateRandomArray(arraySize, uniqueValues);
            
            // Original TwinArray Sort
            int[] originalArray = array.clone();
            System.gc();
            long startMemory = getMemoryUsage();
            long startTime = System.nanoTime();
            twinArraySort(originalArray);
            long endTime = System.nanoTime();
            long endMemory = getMemoryUsage();
            double executionTimeOriginal = (endTime - startTime) / 1e6;
            long memoryUsedOriginal = endMemory - startMemory;
            
            // Optimized TwinArray Sort (Using HashMap)
            int[] optimizedArray = array.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            twinArraySortOptimized(optimizedArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            double executionTimeOptimized = (endTime - startTime) / 1e6;
            long memoryUsedOptimized = endMemory - startMemory;
            
            // Parallel TwinArray Sort
            int[] parallelArray = array.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            twinArraySortParallel(parallelArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            double executionTimeParallel = (endTime - startTime) / 1e6;
            long memoryUsedParallel = endMemory - startMemory;
            
            // Hybrid Sort
            int[] hybridArray = array.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            hybridSort(hybridArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            double executionTimeHybrid = (endTime - startTime) / 1e6;
            long memoryUsedHybrid = endMemory - startMemory;
            
            // Update the output text
            outputArea.setText("TwinArray Sort Results:\n");
            outputArea.append("Array Size: " + arraySize + "\n");
            outputArea.append("Unique Values: " + uniqueValues + "\n\n");
            outputArea.append(String.format("Original TwinArray Sort: %.2f ms, Memory: %.2f KB (O(n + k), O(n + k))\n", 
                executionTimeOriginal, memoryUsedOriginal / 1024.0));
            outputArea.append(String.format("Optimized TwinArray Sort: %.2f ms, Memory: %.2f KB (O(n + k), O(k) where k is the range instead of the max value of the array)\n", 
                executionTimeOptimized, memoryUsedOptimized / 1024.0));
            outputArea.append(String.format("Parallel TwinArray Sort: %.2f ms, Memory: %.2f KB (O(n log n), O(n))\n",   
                executionTimeParallel, memoryUsedParallel / 1024.0));
            outputArea.append(String.format("Hybrid Sort: %.2f ms, Memory: %.2f KB (O(n log n), O(n))\n", 
                executionTimeHybrid, memoryUsedHybrid / 1024.0));
                
            // Test with specific array {2, 3, 4, 10000}
            outputArea.append("\nTesting with array: {2, 3, 4, 10000}\n");
            int[] testArray = {2, 3, 4, 10000};
            
            // Original TwinArray Sort - Test Array
            int[] originalTestArray = testArray.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            twinArraySort(originalTestArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            executionTimeOriginal = (endTime - startTime) / 1e6;
            memoryUsedOriginal = endMemory - startMemory;
            
            // Optimized TwinArray Sort - Test Array
            int[] optimizedTestArray = testArray.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            twinArraySortOptimized(optimizedTestArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            executionTimeOptimized = (endTime - startTime) / 1e6;
            memoryUsedOptimized = endMemory - startMemory;
            
            // Parallel TwinArray Sort - Test Array
            int[] parallelTestArray = testArray.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            twinArraySortParallel(parallelTestArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            executionTimeParallel = (endTime - startTime) / 1e6;
            memoryUsedParallel = endMemory - startMemory;
            
            // Hybrid Sort - Test Array
            int[] hybridTestArray = testArray.clone();
            System.gc();
            startMemory = getMemoryUsage();
            startTime = System.nanoTime();
            hybridSort(hybridTestArray);
            endTime = System.nanoTime();
            endMemory = getMemoryUsage();
            executionTimeHybrid = (endTime - startTime) / 1e6;
            memoryUsedHybrid = endMemory - startMemory;
            
            // Output results for test array
            outputArea.append(String.format("Original TwinArray Sort: %.2f ms, Memory: %.2f KB\n", 
                executionTimeOriginal, memoryUsedOriginal / 1024.0));
            outputArea.append(String.format("Optimized TwinArray Sort: %.2f ms, Memory: %.2f KB\n", 
                executionTimeOptimized, memoryUsedOptimized / 1024.0));
            outputArea.append(String.format("Parallel TwinArray Sort: %.2f ms, Memory: %.2f KB\n", 
                executionTimeParallel, memoryUsedParallel / 1024.0));
            outputArea.append(String.format("Hybrid Sort: %.2f ms, Memory: %.2f KB\n", 
                executionTimeHybrid, memoryUsedHybrid / 1024.0));
        } finally {
            System.gc();  // Cleanup after measurements
        }
    }
    
    private int[] generateRandomArray(int size, int uniqueValues) {
        Random random = new Random();
        int[] array = new int[size];
        
        // First, generate the required unique values
        Set<Integer> uniqueSet = new HashSet<>();
        while (uniqueSet.size() < uniqueValues) {
            uniqueSet.add(random.nextInt(size * 2)); // Larger range for better distribution
        }
        
        // Convert set to array for easy access
        Integer[] uniqueNumbers = uniqueSet.toArray(new Integer[0]);
        
        // Fill the array with random selections from unique values
        for (int i = 0; i < size; i++) {
            array[i] = uniqueNumbers[random.nextInt(uniqueValues)];
        }
        
        return array;
    }
    
    // Original TwinArray Sort
    private void twinArraySort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        int[] values = new int[max + 1];
        int[] frequencies = new int[max + 1];
        
        for (int num : arr) {
            if (frequencies[num] == 0) {
                values[num] = num;
            }
            frequencies[num]++;
        }
        
        int index = 0;
        for (int i = 0; i <= max; i++) {
            while (frequencies[i] > 0) {
                arr[index++] = values[i];
                frequencies[i]--;
            }
        }
    }
    
    // Optimized TwinArray Sort (Using HashMap)
    private void twinArraySortOptimized(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
    
        // Find range
        int min = Arrays.stream(arr).min().orElse(0);
        int max = Arrays.stream(arr).max().orElse(0);
        int range = max - min + 1;
    
        // Create auxiliary arrays
        int[] values = new int[range];
        int[] frequencies = new int[range];
    
        // Count frequencies and store values
        for (int num : arr) {
            int index = num - min;
            if (frequencies[index] == 0) {
                values[index] = num;
            }
            frequencies[index]++;
        }
    
        // Reconstruct the array
        int arrayIndex = 0;
        for (int i = 0; i < range; i++) {
            while (frequencies[i] > 0) {
                arr[arrayIndex++] = values[i];
                frequencies[i]--;
            }
        }
    }
    
    // Parallel TwinArray Sort
    private void twinArraySortParallel(int[] arr) {
        Map<Integer, Integer> frequencyMap = Arrays.stream(arr)
            .parallel()
            .boxed()
            .collect(Collectors.toConcurrentMap(i -> i, i -> 1, Integer::sum));

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : new TreeMap<>(frequencyMap).entrySet()) {
            Arrays.fill(arr, index, index + entry.getValue(), entry.getKey());
            index += entry.getValue();
        }
    }
    
    // Hybrid Sort (Switch to QuickSort if Range is Large)
    private void hybridSort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        int min = Arrays.stream(arr).min().orElse(0);
        int range = max - min;
        
        if (range > 10 * arr.length) {
            Arrays.sort(arr);  // Switch to QuickSort
            return;
        }
        twinArraySortOptimized(arr);
    }
    
    private long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TwinArraySortGUI frame = new TwinArraySortGUI();
            frame.setVisible(true);
        });
    }
}
