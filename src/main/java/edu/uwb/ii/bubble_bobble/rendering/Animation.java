package edu.uwb.ii.bubble_bobble.rendering;

public class Animation {

    private int [] _frames;
    private int _duration;

    private int _frame;
    private int _counter;

    public Animation(int [] frames, double speed) {

        _frames = frames;
        _duration = (int) (120 / speed);

        _frame = 0;
        _counter = _duration;

    }

    public Animation(int offset, int nframes, double speed) {

        _frames = new int[nframes];

        for(int i = 0; i < nframes; i++)
            _frames[i] = offset + i;

        _duration = (int) (120 / speed);

        _frame = 0;
        _counter = _duration;

    }

    public int next() {

        int result = _frame;

        if(--_counter < 1) {

            _counter = _duration;
            _frame = ++_frame < _frames.length ? _frame : 0;

        }

        return result;

    }

}
