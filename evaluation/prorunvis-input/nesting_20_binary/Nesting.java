class Nesting {

    static int compute(int x) {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            x = x + i;
        if (x % 22 == 0) {
            if (x % 21 == 0) {
                if (x % 20 == 0) {
                    if (x % 19 == 0) {
                        if (x % 18 == 0) {
                            if (x % 17 == 0) {
                                if (x % 16 == 0) {
                                    if (x % 15 == 0) {
                                        if (x % 14 == 0) {
                                            if (x % 13 == 0) {
                                                if (x % 12 == 0) {
                                                    if (x % 11 == 0) {
                                                        if (x % 10 == 0) {
                                                            if (x % 9 == 0) {
                                                                if (x % 8 == 0) {
                                                                    if (x % 7 == 0) {
                                                                        if (x % 6 == 0) {
                                                                            if (x % 5 == 0) {
                                                                                if (x % 4 == 0) {
                                                                                    if (x % 3 == 0) {
                                                                                        sum += 1;
                                                                                    } else {
                                                                                        sum += 1;
                                                                                    }
                                                                                } else {
                                                                                    sum += 2;
                                                                                }
                                                                            } else {
                                                                                sum += 3;
                                                                            }
                                                                        } else {
                                                                            sum += 4;
                                                                        }
                                                                    } else {
                                                                        sum += 5;
                                                                    }
                                                                } else {
                                                                    sum += 6;
                                                                }
                                                            } else {
                                                                sum += 7;
                                                            }
                                                        } else {
                                                            sum += 8;
                                                        }
                                                    } else {
                                                        sum += 9;
                                                    }
                                                } else {
                                                    sum += 10;
                                                }
                                            } else {
                                                sum += 11;
                                            }
                                        } else {
                                            sum += 12;
                                        }
                                    } else {
                                        sum += 13;
                                    }
                                } else {
                                    sum += 14;
                                }
                            } else {
                                sum += 15;
                            }
                        } else {
                            sum += 16;
                        }
                    } else {
                        sum += 17;
                    }
                } else {
                    sum += 18;
                }
            } else {
                sum += 19;
            }
        } else {
            sum += 20;
        }
        }
        return sum;
    }

    public static void main(String[] args) {
        int result = compute(42);
        System.out.println("result = " + result);
    }
}
