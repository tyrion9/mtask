package com.github.tyrion9.mtask;

public abstract class MTask implements Runnable{
    protected boolean interrupted = false;

    protected final MLogger mlog = new MLogger();


}
