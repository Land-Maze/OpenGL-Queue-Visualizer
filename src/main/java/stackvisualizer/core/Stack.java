package stackvisualizer.core;

// import java.util.ArrayList;
// import java.util.List;

// public class Stack {
//     private List<Cube> cubes = new ArrayList<>();
//     private float formHeight = 0.0f;

//     public void push(Cube cube) {
//         cube.setPosition(0, formHeight, 0);
//         cubes.add(cube);
//         formHeight += cube.getHeight();
//     }

//     public Cube pop() {
//         if (cubes.isEmpty()) return null;
//         Cube cube = cubes.remove(cubes.size() - 1);
//         formHeight -= cube.getHeight();
//         return cube;
//     }

//     public Cube peek() {
//         return cubes.isEmpty() ? null : cubes.get(cubes.size() - 1);
//     }

//     public boolean isEmpty() {
//         return cubes.isEmpty();
//     }

//     public List<Cube> getCubes() {
//         return cubes;
//     }

//     public float getFormHeight() {
//         return formHeight;
//     }

//     public void updatePhysics() {
//         for (Cube cube : cubes) {
//             cube.update();
//         }
//     }
// }