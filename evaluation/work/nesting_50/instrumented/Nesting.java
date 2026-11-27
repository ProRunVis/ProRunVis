class Nesting {

    static int compute(int x) {
        srctracer.Trace._FUNC(1);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            srctracer.Trace._LOOP_BODY();
            x = x + i;
            if (x % 52 == 0) {
                srctracer.Trace._IF();
                if (x % 51 == 0) {
                    srctracer.Trace._IF();
                    if (x % 50 == 0) {
                        srctracer.Trace._IF();
                        if (x % 49 == 0) {
                            srctracer.Trace._IF();
                            if (x % 48 == 0) {
                                srctracer.Trace._IF();
                                if (x % 47 == 0) {
                                    srctracer.Trace._IF();
                                    if (x % 46 == 0) {
                                        srctracer.Trace._IF();
                                        if (x % 45 == 0) {
                                            srctracer.Trace._IF();
                                            if (x % 44 == 0) {
                                                srctracer.Trace._IF();
                                                if (x % 43 == 0) {
                                                    srctracer.Trace._IF();
                                                    if (x % 42 == 0) {
                                                        srctracer.Trace._IF();
                                                        if (x % 41 == 0) {
                                                            srctracer.Trace._IF();
                                                            if (x % 40 == 0) {
                                                                srctracer.Trace._IF();
                                                                if (x % 39 == 0) {
                                                                    srctracer.Trace._IF();
                                                                    if (x % 38 == 0) {
                                                                        srctracer.Trace._IF();
                                                                        if (x % 37 == 0) {
                                                                            srctracer.Trace._IF();
                                                                            if (x % 36 == 0) {
                                                                                srctracer.Trace._IF();
                                                                                if (x % 35 == 0) {
                                                                                    srctracer.Trace._IF();
                                                                                    if (x % 34 == 0) {
                                                                                        srctracer.Trace._IF();
                                                                                        if (x % 33 == 0) {
                                                                                            srctracer.Trace._IF();
                                                                                            if (x % 32 == 0) {
                                                                                                srctracer.Trace._IF();
                                                                                                if (x % 31 == 0) {
                                                                                                    srctracer.Trace._IF();
                                                                                                    if (x % 30 == 0) {
                                                                                                        srctracer.Trace._IF();
                                                                                                        if (x % 29 == 0) {
                                                                                                            srctracer.Trace._IF();
                                                                                                            if (x % 28 == 0) {
                                                                                                                srctracer.Trace._IF();
                                                                                                                if (x % 27 == 0) {
                                                                                                                    srctracer.Trace._IF();
                                                                                                                    if (x % 26 == 0) {
                                                                                                                        srctracer.Trace._IF();
                                                                                                                        if (x % 25 == 0) {
                                                                                                                            srctracer.Trace._IF();
                                                                                                                            if (x % 24 == 0) {
                                                                                                                                srctracer.Trace._IF();
                                                                                                                                if (x % 23 == 0) {
                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                    if (x % 22 == 0) {
                                                                                                                                        srctracer.Trace._IF();
                                                                                                                                        if (x % 21 == 0) {
                                                                                                                                            srctracer.Trace._IF();
                                                                                                                                            if (x % 20 == 0) {
                                                                                                                                                srctracer.Trace._IF();
                                                                                                                                                if (x % 19 == 0) {
                                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                                    if (x % 18 == 0) {
                                                                                                                                                        srctracer.Trace._IF();
                                                                                                                                                        if (x % 17 == 0) {
                                                                                                                                                            srctracer.Trace._IF();
                                                                                                                                                            if (x % 16 == 0) {
                                                                                                                                                                srctracer.Trace._IF();
                                                                                                                                                                if (x % 15 == 0) {
                                                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                                                    if (x % 14 == 0) {
                                                                                                                                                                        srctracer.Trace._IF();
                                                                                                                                                                        if (x % 13 == 0) {
                                                                                                                                                                            srctracer.Trace._IF();
                                                                                                                                                                            if (x % 12 == 0) {
                                                                                                                                                                                srctracer.Trace._IF();
                                                                                                                                                                                if (x % 11 == 0) {
                                                                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                                                                    if (x % 10 == 0) {
                                                                                                                                                                                        srctracer.Trace._IF();
                                                                                                                                                                                        if (x % 9 == 0) {
                                                                                                                                                                                            srctracer.Trace._IF();
                                                                                                                                                                                            if (x % 8 == 0) {
                                                                                                                                                                                                srctracer.Trace._IF();
                                                                                                                                                                                                if (x % 7 == 0) {
                                                                                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                                                                                    if (x % 6 == 0) {
                                                                                                                                                                                                        srctracer.Trace._IF();
                                                                                                                                                                                                        if (x % 5 == 0) {
                                                                                                                                                                                                            srctracer.Trace._IF();
                                                                                                                                                                                                            if (x % 4 == 0) {
                                                                                                                                                                                                                srctracer.Trace._IF();
                                                                                                                                                                                                                if (x % 3 == 0) {
                                                                                                                                                                                                                    srctracer.Trace._IF();
                                                                                                                                                                                                                    sum += 1;
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                                                                                                    sum += 1;
                                                                                                                                                                                                                }
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                                                                                                sum += 2;
                                                                                                                                                                                                            }
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                                                                                                            sum += 3;
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                                                                                                        sum += 4;
                                                                                                                                                                                                    }
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                                                                                    sum += 5;
                                                                                                                                                                                                }
                                                                                                                                                                                            } else {
                                                                                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                                                                                sum += 6;
                                                                                                                                                                                            }
                                                                                                                                                                                        } else {
                                                                                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                                                                                            sum += 7;
                                                                                                                                                                                        }
                                                                                                                                                                                    } else {
                                                                                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                                                                                        sum += 8;
                                                                                                                                                                                    }
                                                                                                                                                                                } else {
                                                                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                                                                    sum += 9;
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                                                                sum += 10;
                                                                                                                                                                            }
                                                                                                                                                                        } else {
                                                                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                                                                            sum += 11;
                                                                                                                                                                        }
                                                                                                                                                                    } else {
                                                                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                                                                        sum += 12;
                                                                                                                                                                    }
                                                                                                                                                                } else {
                                                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                                                    sum += 13;
                                                                                                                                                                }
                                                                                                                                                            } else {
                                                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                                                sum += 14;
                                                                                                                                                            }
                                                                                                                                                        } else {
                                                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                                                            sum += 15;
                                                                                                                                                        }
                                                                                                                                                    } else {
                                                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                                                        sum += 16;
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                                    sum += 17;
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                                sum += 18;
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                                            sum += 19;
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                                        sum += 20;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                                    sum += 21;
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                srctracer.Trace._ELSE();
                                                                                                                                sum += 22;
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            srctracer.Trace._ELSE();
                                                                                                                            sum += 23;
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        srctracer.Trace._ELSE();
                                                                                                                        sum += 24;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    srctracer.Trace._ELSE();
                                                                                                                    sum += 25;
                                                                                                                }
                                                                                                            } else {
                                                                                                                srctracer.Trace._ELSE();
                                                                                                                sum += 26;
                                                                                                            }
                                                                                                        } else {
                                                                                                            srctracer.Trace._ELSE();
                                                                                                            sum += 27;
                                                                                                        }
                                                                                                    } else {
                                                                                                        srctracer.Trace._ELSE();
                                                                                                        sum += 28;
                                                                                                    }
                                                                                                } else {
                                                                                                    srctracer.Trace._ELSE();
                                                                                                    sum += 29;
                                                                                                }
                                                                                            } else {
                                                                                                srctracer.Trace._ELSE();
                                                                                                sum += 30;
                                                                                            }
                                                                                        } else {
                                                                                            srctracer.Trace._ELSE();
                                                                                            sum += 31;
                                                                                        }
                                                                                    } else {
                                                                                        srctracer.Trace._ELSE();
                                                                                        sum += 32;
                                                                                    }
                                                                                } else {
                                                                                    srctracer.Trace._ELSE();
                                                                                    sum += 33;
                                                                                }
                                                                            } else {
                                                                                srctracer.Trace._ELSE();
                                                                                sum += 34;
                                                                            }
                                                                        } else {
                                                                            srctracer.Trace._ELSE();
                                                                            sum += 35;
                                                                        }
                                                                    } else {
                                                                        srctracer.Trace._ELSE();
                                                                        sum += 36;
                                                                    }
                                                                } else {
                                                                    srctracer.Trace._ELSE();
                                                                    sum += 37;
                                                                }
                                                            } else {
                                                                srctracer.Trace._ELSE();
                                                                sum += 38;
                                                            }
                                                        } else {
                                                            srctracer.Trace._ELSE();
                                                            sum += 39;
                                                        }
                                                    } else {
                                                        srctracer.Trace._ELSE();
                                                        sum += 40;
                                                    }
                                                } else {
                                                    srctracer.Trace._ELSE();
                                                    sum += 41;
                                                }
                                            } else {
                                                srctracer.Trace._ELSE();
                                                sum += 42;
                                            }
                                        } else {
                                            srctracer.Trace._ELSE();
                                            sum += 43;
                                        }
                                    } else {
                                        srctracer.Trace._ELSE();
                                        sum += 44;
                                    }
                                } else {
                                    srctracer.Trace._ELSE();
                                    sum += 45;
                                }
                            } else {
                                srctracer.Trace._ELSE();
                                sum += 46;
                            }
                        } else {
                            srctracer.Trace._ELSE();
                            sum += 47;
                        }
                    } else {
                        srctracer.Trace._ELSE();
                        sum += 48;
                    }
                } else {
                    srctracer.Trace._ELSE();
                    sum += 49;
                }
            } else {
                srctracer.Trace._ELSE();
                sum += 50;
            }
        }
        srctracer.Trace._LOOP_END();
        {
            int __srctracer_ret$0 = sum;
            srctracer.Trace._RETURN();
            return __srctracer_ret$0;
        }
    }

    public static void main(String[] args) {
        srctracer.Trace.trace_start("Nesting");
        try {
            srctracer.Trace._FUNC(2);
            int result = compute(42);
            System.out.println("result = " + result);
        } finally {
            srctracer.Trace.trace_end();
        }
    }
}
