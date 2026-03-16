// Student  : TINSAY, JOHN CLEO T.
// Course   : Math 101 – Linear Algebra, UPHSD Molino Campus
// Assignment: Assignment 01 – 3x3 Matrix Determinant Solver
// Date     : 2025
// Description: Computes the determinant of the assigned 3x3 matrix
//              using cofactor expansion along the first row.

function computeMinor(a, b, c, d) {
    return (a * d) - (b * c);
}

function solveDeterminant(M) {
    const minor11 = computeMinor(M[1][1], M[1][2], M[2][1], M[2][2]);
    const minor12 = computeMinor(M[1][0], M[1][2], M[2][0], M[2][2]);
    const minor13 = computeMinor(M[1][0], M[1][1], M[2][0], M[2][1]);

    const c11 =  M[0][0] * minor11;
    const c12 = -M[0][1] * minor12;
    const c13 =  M[0][2] * minor13;
    const det = c11 + c12 + c13;

    let output = "";
    output += "===================================================\n";
    output += "  3x3 MATRIX DETERMINANT SOLVER\n";
    output += "  Student: TINSAY, JOHN CLEO T.\n";
    output += "===================================================\n";
    output += `  |  ${M[0][0]}   ${M[0][1]}   ${M[0][2]}  |\n`;
    output += `  |  ${M[1][0]}   ${M[1][1]}   ${M[1][2]}  |\n`;
    output += `  |  ${M[2][0]}   ${M[2][1]}   ${M[2][2]}  |\n`;
    output += "===================================================\n\n";
    output += "Expanding along Row 1:\n\n";
    output += `  Step 1 - Minor M11: (${M[1][1]}x${M[2][2]}) - (${M[1][2]}x${M[2][1]}) = ${minor11}\n`;
    output += `  Step 2 - Minor M12: (${M[1][0]}x${M[2][2]}) - (${M[1][2]}x${M[2][0]}) = ${minor12}\n`;
    output += `  Step 3 - Minor M13: (${M[1][0]}x${M[2][1]}) - (${M[1][1]}x${M[2][0]}) = ${minor13}\n\n`;
    output += `  C11 = (+1) x ${M[0][0]} x ${minor11} = ${c11}\n`;
    output += `  C12 = (-1) x ${M[0][1]} x ${minor12} = ${c12}\n`;
    output += `  C13 = (+1) x ${M[0][2]} x ${minor13} = ${c13}\n\n`;
    output += `  det(M) = ${c11} + (${c12}) + ${c13}\n\n`;
    output += "===================================================\n";
    output += `  DETERMINANT = ${det}\n`;
    output += "===================================================\n";
    if (det === 0) output += "  The matrix is SINGULAR - it has no inverse.\n";

    return output;
}

const matrix = [
    [2, 1, 6],
    [4, 3, 5],
    [6, 2, 4]
];

document.getElementById("output").textContent = solveDeterminant(matrix);
