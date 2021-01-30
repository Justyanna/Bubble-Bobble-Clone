package edu.uwb.ii.bubble_bobble.game.rendering;

import edu.uwb.ii.bubble_bobble.scenes.game.Game;

public class Animation
{

    private int[] _frames;
    private double _speed;
    private int _duration;

    private int _frame;
    private int _counter;

    public Animation(int[] frames, double speed)
    {
        _frames = frames;
        _speed = speed;
        _duration = (int) (Game.FRAME_RATE / speed);

        reset();
    }

    public Animation(int offset, int nframes, double speed)
    {
        _frames = new int[nframes];

        for(int i = 0; i < nframes; i++)
        { _frames[i] = offset + i; }

        _speed = speed;
        _duration = (int) (60 / speed);

        reset();
    }

    public int[] getFrames()
    {
        return _frames.clone();
    }

    public void set_frames(int[] frames)
    {
        _frames = frames;
    }

    public void set_speed(int speed)
    {
        _speed = speed;
        _duration = (int) (Game.FRAME_RATE / speed);
    }

    public Animation copy(int speed)
    {
        return new Animation(_frames, speed);
    }

    public Animation copy()
    {
        return new Animation(_frames, _speed);
    }

//    -- interface

    public int next()
    {
        int result = _frames[_frame];

        if(_speed > 0 && _frames.length > 1 && --_counter < 1)
        {

            _counter = _duration;
            _frame = ++_frame < _frames.length ? _frame : 0;
        }

        return result;
    }

    public void reset()
    {

        _frame = 0;
        _counter = _duration;
    }
}
